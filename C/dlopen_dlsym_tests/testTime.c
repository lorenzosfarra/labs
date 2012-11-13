#include <stdio.h>
#include "timelab.h"

int main(int argc, char **argv) {
  time_t sec; 
  sec = time(NULL);
  // The following printf will print 0 if we're using the custom time()
  printf("%ld\n\n", sec / 3600);
  return 0;
}
