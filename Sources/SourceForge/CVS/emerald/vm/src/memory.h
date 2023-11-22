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
 File     : memory.h
 Date     : 06-22-92
 Author   : Mark Immel

 Contents : Memory allocation functions

 Modifications
 -------------

*****************************************************************************/

#ifndef _EMERALD_MEMORY_H
#define _EMERALD_MEMORY_H

#include "threads.h"

extern int strncmp(const char *, const char *, size_t);
extern void *gc_malloc(int);
extern void *gc_malloc_nogc(int), *gc_malloc_old(int nb, int remember);
extern void *extraRoots[];
extern int     extraRootsSP;
#define regRoot(x) (extraRoots[extraRootsSP++] = (void *)&(x))
#define unregRoot() ( extraRoots[--extraRootsSP] = 0)
#endif /* _EMERALD_MEMORY_H */

