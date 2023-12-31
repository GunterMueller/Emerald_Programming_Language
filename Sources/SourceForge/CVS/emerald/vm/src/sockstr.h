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

/****************************************************************************
 File     : socketstreams.h 
 Date     : 08-11-92
 Author   : Mark Immel

 Contents : Socket Streams package

 Modifications
 -------------

*****************************************************************************/

#ifndef _EMERALD_SOCKETSTREAMS_H
#define _EMERALD_SOCKETSTREAMS_H

/*
  This module provides a stream implementation for sockets.

  The Hook argument to CreateStream should be the address of a integer
  containing the file descriptor associated with the socket to be used.

  All sockets given to these routines should be marked non-blocking.
*/

#include "streams.h"
#include "types.h"

extern StreamConstructor ReadSocketStream;
extern StreamConstructor WriteSocketStream;
extern void              ProcessNewSocketData(Socket theSocket);

#endif /* _EMERALD_SOCKESTREAMS_H */
