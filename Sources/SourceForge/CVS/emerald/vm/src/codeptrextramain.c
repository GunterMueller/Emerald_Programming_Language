#include <stdlib.h>
#include <stdio.h>

int main( int argc, char **argv )
{
	unsigned long x;
	x = strtoul(argv[1], 0, 16);
	printf("%#x",x & 0xfc000000);
}
