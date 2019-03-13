#include <stdio.h>

void value(long long a) {
    if (a == 6) {
        puts("a is 6");
    } else if (a == 7) {
        puts("a is 7");
    } else if (a > 0) {
        puts("a is positive");
    } else {
        puts("a is negative");
    }
}

int main() {
    value(6);
    value(7);
    value(8);
    value(10);
    value(-283);
    return 0;
}
