/*********************************************************************************
 *  TotalCross Software Development Kit                                          *
 *  Copyright (C) 2000-2012 SuperWaba Ltda.                                      *
 *  All Rights Reserved                                                          *
 *                                                                               *
 *  This library and virtual machine is distributed in the hope that it will     *
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                         *
 *                                                                               *
 *********************************************************************************/



#include "Net.h"

#if defined PALMOS
 #include "palm/Socket_c.h"
#elif defined WINCE || defined WIN32
 #include "win/Socket_c.h"
#else
 #include "posix/Socket_c.h"
#endif

static void invalidate(Object obj)
{
   if (Socket_socketRef(obj) != null)
   {
      setObjectLock(Socket_socketRef(obj), UNLOCKED);
      Socket_socketRef(obj) = null;
   }
   Socket_dontFinalize(obj) = true;
}

//////////////////////////////////////////////////////////////////////////
TC_API void tnS_socketCreate_siib(NMParams p) // totalcross/net/Socket native void socketCreate(final String host, final int port, final int timeout, final boolean noLinger);
{
   Object socket = p->obj[0];
   Object socketRef;
   SOCKET* socketHandle;
   Object host = p->obj[1];
   int32 port = p->i32[0];
   int32 timeout = p->i32[1];
   bool noLinger = p->i32[2];
   CharP szHost;
   bool isUnknownHost = false;
   bool timedOut = false;
   Err err;

   if (host == null)
   {
      szHost = (CharP) xmalloc(10);
      if (szHost != null)
         xstrcpy(szHost, "localhost");
   }
   else szHost = String2CharP(host);
   if (szHost == null)
   {
      throwException(p->currentContext, OutOfMemoryError, null);
      return;
   }

   if ((socketRef = createByteArray(p->currentContext, sizeof(SOCKET))) != null)
   {
      Socket_socketRef(socket) = socketRef;
      socketHandle = (SOCKET*) ARRAYOBJ_START(socketRef);
      if ((err = socketCreate(socketHandle, szHost, port, timeout, noLinger, &isUnknownHost, &timedOut)) != NO_ERROR)
      {
         if (isUnknownHost)
            throwException(p->currentContext, UnknownHostException, szHost);
         else if (timedOut)
            throwException(p->currentContext, IOException, "Socket creation timed out.");
         else
            throwExceptionWithCode(p->currentContext, IOException, err);
         invalidate(socket);
      }
   }
   xfree(szHost);
}
//////////////////////////////////////////////////////////////////////////
TC_API void tnS_nativeClose(NMParams p) // totalcross/net/Socket native private void nativeClose() throws totalcross.io.IOException;
{
   Object socket = p->obj[0];
   Object socketRef = Socket_socketRef(socket);
   SOCKET* socketHandle = (SOCKET*) ARRAYOBJ_START(socketRef);
   Err err;

   if ((err = socketClose(socketHandle)) != NO_ERROR)
      throwExceptionWithCode(p->currentContext, IOException, err);
   invalidate(socket);
}
//////////////////////////////////////////////////////////////////////////
TC_API void tnS_readWriteBytes_Biib(NMParams p) // totalcross/net/Socket native private int readWriteBytes(byte []buf, int start, int count, boolean isRead) throws totalcross.io.IOException;
{
   Object socket = p->obj[0];
   Object socketRef = Socket_socketRef(socket);
   SOCKET* socketHandle;
   Object buf = p->obj[1];
   int32 start = p->i32[0];
   int32 count = p->i32[1];
   bool isRead = p->i32[2];
   int32 timeout;
   int32 retCount;
   Err err;

   if (isRead)
      timeout = Socket_readTimeout(socket);
   else
      timeout = Socket_writeTimeout(socket);

   socketHandle = (SOCKET*) ARRAYOBJ_START(socketRef);
   if ((err = socketReadWriteBytes(*socketHandle, timeout, ARRAYOBJ_START(buf), start, count, &retCount, isRead)) != NO_ERROR)
      throwExceptionWithCode(p->currentContext, IOException, err);
   else if (retCount == -2) // timeout!
      throwException(p->currentContext, SocketTimeoutException, "Operation timed out");
   else
      p->retI = retCount;
}
//////////////////////////////////////////////////////////////////////////
// Used by axTLS as socket I/O function.
int tcSocketReadWrite(int fd, CharP buf, int32 count, bool isRead)
{
   int32 retCount;
   Object socket;
   int32 timeout;
   Err err;

   LOCKVAR(htSSL);
   socket = (Object) htGetPtr(&htSSLSocket, fd);
   UNLOCKVAR(htSSL);

   if (!socket) // guich@tc113_14
      return -1;

   if (isRead)
      timeout = Socket_readTimeout(socket);
   else
      timeout = Socket_writeTimeout(socket);

   err = socketReadWriteBytes(fd, timeout, buf, 0, count, &retCount, isRead);
   return (err == NO_ERROR) ? retCount : -1;
}

#ifdef ENABLE_TEST_SUITE
#include "Socket_test.h"
#endif