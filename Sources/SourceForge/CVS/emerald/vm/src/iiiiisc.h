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

/*
 * IIIIIScs (searchable collections) are things that map 
 * elements of some domain onto some range.  Operations:
 *	create, destroy, insert, lookup, size, and print
 */

#ifndef _EMERALD_IIIIISC_H
#define _EMERALD_IIIIISC_H

#include "memory.h"

/*
 * This one is crafted differently from all others in that it takes its key
 as separate integers, and so needed a fair amount of hand editing.
 */
typedef struct {
  int a, b, c, d;
} IIIIIScDomainType;
#define IIIIIScRangeType  int
#define IIIIIScHASH(W,X,Y,Z) ((unsigned)((W) ^ ((X)<<4) ^ (Y) ^ ((Z) >> 4)))
#define IIIIIScCOMPARE(K,W,X,Y,Z) ((K).a==(W)&&(K).b==(X)&&(K).c==(Y)&&(K).d==(Z))
#define IIIIIScNIL (-1)

/*
 * Hidden, private type declarations.  The only thing
 * that applications of this package are to see is IIIIISc,
 * and they are to treat it as opaque:  that is, they may
 * assign it, and pass it as arguments, but not manipulate
 * what it points to directly.
 */

typedef struct IIIIIScTE {
    IIIIIScDomainType	 key;		/* the key for this entry */
    IIIIIScRangeType	 value;		/* what we want */
} IIIIIScTE, *IIIIIScTEPtr;

typedef struct IIIIIScRecord {
    IIIIIScTEPtr table;
    int size, maxCount, count;
} IIIIIScRecord, *IIIIISc;

/* OPERATIONS */

/* Return a new, empty Searchable Collection */
IIIIISc IIIIIScCreate(void);

/* Destroy a collection */
void IIIIIScDestroy(IIIIISc sc);

/* Insert the pair <key, value> into collection IIIIISc */
void IIIIIScInsert(/* IIIIISc sc, int a, int b, int c, int d, IIIIIScRangeType value */);

/* bump the value associated with some given key in the IIIIISc */
int IIIIIScBump(/* IIIIISc sc, int a, int b, int c, int d*/);

/* bump the value associated with some given key in the IIIIISc by value */
int IIIIIScBumpBy(/* IIIIISc sc, int a, int b, int c, int d, int value*/);

/* Delete the pair with key key from the collection IIIIISc */
void IIIIIScDelete(/* IIIIISc sc, int a, int b, int c, int d */);

/* Return the value associated with key in collection 
 * IIIIISc, or 0 if no such pair exists */
int IIIIIScLookup(/* IIIIISc sc, int a, int b, int c, int d */);

/* DEBUGGING: Print the collection IIIIISc */
void IIIIIScPrint(/* IIIIISc sc */);

/* Iterate over the elements of the collection IIIIISc.  
 * At each iteration, IIIIISckey and IIIIIScvalue are set to the next
 * <key, value> pair in the collection.  
 * Usage:
 *	IIIIIScForEach(someSc, key, value) {
 *	  / * whatever you want to do with key, value * /
 *	} IIIIIScNext();
 */
#define IIIIIScForEach(IIIIISc, IIIIISckeya, IIIIISckeyb, IIIIISckeyc, IIIIIISckeyd, IIIIIScvalue) \
  { \
    int IIIIIScxx_index; \
    for (IIIIIScxx_index = 0; IIIIIScxx_index < (IIIIISc)->size; IIIIIScxx_index++) { \
      if ((IIIIISc)->table[IIIIIScxx_index].key != IIIIIScNIL) { \
	*(int*)(&(IIIIISckeya)) = (IIIIISc)->table[IIIIIScxx_index].key.a; \
	*(int*)(&(IIIIISckeyb)) = (IIIIISc)->table[IIIIIScxx_index].key.b; \
	*(int*)(&(IIIIISckeyc)) = (IIIIISc)->table[IIIIIScxx_index].key.c; \
	*(int*)(&(IIIIISckeyd)) = (IIIIISc)->table[IIIIIScxx_index].key.d; \
	*(IIIIIScRangeType *)(&(IIIIIScvalue)) = (IIIIISc)->table[IIIIIScxx_index].value; \
	{ 

#define IIIIIScNext() \
	} \
      } \
    } \
  }

/* Return the number of elements in IIIIISc */
#define IIIIIScSize(IIIIISc) ((IIIIISc)->count)

#endif /* _EMERALD_IIIIISC_H */
