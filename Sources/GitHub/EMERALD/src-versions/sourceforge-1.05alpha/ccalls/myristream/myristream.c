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

/* myristreams.c - Margaret Dumont
 *
 * The minimal interface that allows Emerald objects on the lanai to print to
 * stdout. 
*/

#include "baselib.h"
#include "trace.h"

typedef struct Vector {
  int dummy1;
  struct {
    int items;
    char data[4];
  } d;
} *Vector;
#include "myristream.h"

int
streamOpen( int *fail, char *url )
{
  return (1);
}

int
streamEos( int *fail, int fd )
{
  return (1);
}

char
streamGetChar( int *fail, int fd )
{
  return 0;
}

void
streamUngetChar( int *fail, int fd, char c )
{

  return ;
}

char *
streamGetString( int *fail, int fd )
{
  return 0;
}

int
streamFillVector( int *fail, int fd, int v )
{
  return (1);
}

int
streamIsAtty( int *fail, int fd )
{
  return (1);
}

static int
streamWrite( int fd, char *buf, int len )
{/*
  ftrace(buf);
  */
  char buffer[128];
 

  strcpy(buffer, "lanai: ");
  strcat(buffer+7, buf);
  strcat(buffer,"\n");

  MCP_debug(buffer, strlen(buffer));
  return (1);
}

void
streamPutChar( int *fail, int fd, char c )
{
  return ;
}

void
streamPutInt( int *fail, int fd, int n, int width )
{
  return ;
}

void
streamPutReal( int *fail, int fd, int n)
{
  return ;
}

void
streamWriteInt( int *fail, int fd, int n, int bytes )
{
  return ;
}

void
streamPutString( int *fail, int fd, int b )
{
  Vector buf = (Vector) b;
  int rval;

  rval = streamWrite( fd, buf->d.data, buf->d.items );
  return ;
}

void
streamFlush( int *fail, int fd )
{
  return ;
}

int
streamBind( char *url )
{
  return (1);
}

int
streamAccept( int boundSocket )
{
  return (1);
}

void
streamClose( int *fail, int fd )
{
  return ;
}

/* EOF */
