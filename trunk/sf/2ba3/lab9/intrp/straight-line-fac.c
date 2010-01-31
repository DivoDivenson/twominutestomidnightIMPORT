/* A factorial program */

int
main()
{
    int i;
    int fac;
    int c;
    i = 1;
    fac = 1;

    fac = fac * i;
    i++;

    fac = fac * i;
    i++;

    fac = fac * i;
    i++;

    fac = fac * i;
    i++;

    fac = fac * i;
    i++;

    fac = fac * i;
    i++;

    fac = fac * i;
    i++;

    c = (fac%10) + '0';
    putchar(c);
    fac = fac/10;

    c = (fac%10) + '0';
    putchar(c);
    fac = fac/10;

    c = (fac%10) + '0';
    putchar(c);
    fac = fac/10;

    c = (fac%10) + '0';
    putchar(c);
    fac = fac/10;

    putchar('\n');
    exit(0);

    return 0;
}
