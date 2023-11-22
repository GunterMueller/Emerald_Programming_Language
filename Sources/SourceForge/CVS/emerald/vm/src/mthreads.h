/*
 * The Emerald Distributed Programming Language
 * 
 * Copyright (C) 2004 Emerald Authors & Contributors
 * 
 * This file is part of the Emerald Distributed Programming Language.
 *
 * The Emerald Distributed Programming Language is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 *  The Emerald Distributed Programming Language is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with the Emerald Distributed Programming Language; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */

#ifndef _EMERALD_MTHREADS_H
#define _EMERALD_MTHREADS_H
#pragma warning(disable: 4068)
#pragma pointer_size save
#pragma pointer_size long
#include <sys/types.h>

#ifndef WIN32
#include <sys/time.h>
#include <stdarg.h>
#include <sys/socket.h>
#include <netinet/in.h>
#endif
#pragma pointer_size restore

#ifdef WIN32
#ifdef MSVC40
#include <winsock.h>
#else
#include <winsock2.h>
#endif
#endif

typedef struct NodeAddr {
  unsigned int ipaddress;
  unsigned short port;
  unsigned short incarnation;
} NodeAddr;

extern void MTInit(void);
void MTStart(void);

void MTRegisterExitRoutine(void (*)(void));

int MTNetStart(unsigned int, unsigned short, unsigned short);
int MTSend(NodeAddr receiver, void *sbuf, int slen);
int MTProd(NodeAddr *receiver);
typedef void (*NotifyFunction)(NodeAddr id, int isup);
void MTRegisterNotify(NotifyFunction);
#endif /* _EMERALD_MTHREADS_H */
