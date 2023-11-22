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

/*#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include <assert.h>
#include "/grads/harrison/538/Gui/client.h"
#include "/grads/harrison/538/Gui/server.h"
#include "/grads/harrison/538/Gui/util.h"
#include "/grads/harrison/538/Gui/header.h"*/

/*----------------------------- DO NOT MODIFY BELOW THIS LINE ----------------------*/

void startServer(void)
{
  printf("server started\n");
}

int callServer(int a, int b)
{
  return a * b;
}

void startClient(void)
{
  int a, b, c, i;
  printf("client started\n");
  for (i = 0; i < 10; i++) {
    a = random() % 5;
    b = random() % 5;
    c = random() % 5;
    printf("callEmerald[%d, %d, %d, %d] -> %d\n",
	   i, a, b, c, callEmerald(3, i, a, b, c));
  }
}
void registerClient(void *theClientObjP)
{
  extern void *theClientObj;
  theClientObj = theClientObjP;
  printf("client registered %x\n", theClientObj);
}

/*---------------------------- DO NOT MODIFY ABOVE THIS LINE -----------------------*/
/*int InitClient( int i , char * filename)
{
	printf("Client initialized with %d and %s\n", i, filename);
	return 0;
}

int RunClient(int i)
{
	printf("running client %d\n", i);
	printf("\n\nCalling EMERALD! %d\n\n" , callEmerald(3,1,2,3,4));
	return 0;
}


int CallServer(int a, int b, int c, int d)
{
	printf("Called server\n");
	callEmerald(3,1,2,3,4);
	return 0;
}
void InitServer(int j)
{
	printf("Initialized Server with %d clients\n", j);
}
*/


void Replying(char * c)
{
	printf("%s\n");
}

void Tester(void)
{

	printf("******  C calling Emerald\n");
}
	
