#include <stdio.h>
#include <string.h>

int
eputchar(int c)
{
    return fputc(c, stderr);
}

#include "defs.h"
#define MAXSTACK 10000
#define MAXMEM (MAXCODE+MAXDATA+MAXSTACK)

int mem[MAXMEM];

int verbosef;

#define code mem
#include "dumpcode.c"

int
main(int argc, char **argv)
{
    int *q, sp, fp, r, opc, lit, pc, codesize;
    int oldpc;

    verbosef = argc > 1;

    codesize = getchar();
    codesize = codesize + getchar()*256;
    pc = codesize;
    q = mem;
    /* read the code */
    while(pc--)
	*q++ = getchar();

    if (verbosef)
	fprintf(stderr, "code size %d\n", codesize);
    sp = MAXMEM;
    pc = 0;
    /* Start execution */
    while(1) {
	if (verbosef) {
	    fprintf(stderr, "%5d %5d(%5d,%5d,%5d) ", fp, sp, mem[sp], mem[sp+1], mem[sp+2]);
	    dumpcode(pc, pc+1);
	}
	oldpc = pc;
	switch (opc = mem[pc++]) {
	  /* unary operators */
	case C_NOT: mem[sp] = ! mem[sp]; break;
	case C_NEG: mem[sp] = - mem[sp]; break;
	  /* binary operators */
	case C_ADD: mem[sp+1] = mem[sp+1] + mem[sp]; sp++; break;
        case C_SUB: mem[sp+1] = mem[sp+1] - mem[sp]; sp++; break;
        case C_MUL: mem[sp+1] = mem[sp+1] * mem[sp]; sp++; break;
        case C_DIV: mem[sp+1] = mem[sp+1] / mem[sp]; sp++; break;
        case C_MOD: mem[sp+1] = mem[sp+1] % mem[sp]; sp++; break;
        case C_AND: mem[sp+1] = mem[sp+1] & mem[sp]; sp++; break;
        case C_OR: mem[sp+1] = mem[sp+1] | mem[sp]; sp++; break;
        case C_GT: mem[sp+1] = mem[sp+1] > mem[sp]; sp++; break;
        case C_GE: mem[sp+1] = mem[sp+1] >= mem[sp]; sp++; break;
        case C_LE: mem[sp+1] = mem[sp+1] <= mem[sp]; sp++; break;
        case C_EQ: mem[sp+1] = mem[sp+1] == mem[sp]; sp++; break;
        case C_NE: mem[sp+1] = mem[sp+1] != mem[sp]; sp++; break;
        case C_LT: mem[sp+1] = mem[sp+1] < mem[sp]; sp++; break;

	case C_ASSIGN: r = mem[sp++]; mem[mem[sp]] = r; mem[sp] = r; break;
	case C_ASSIGNPOP: r = mem[sp++]; mem[mem[sp]] = r; sp++; break;
	case C_RETURN:
	     r = mem[sp];
	     sp = fp;
	     pc = mem[sp-F_PC];
	     fp = mem[sp-F_FP];
	     lit = mem[sp-F_NA];
	     sp = sp + lit/2;mem[sp] = r;if (lit%2) sp++; /* do return */
	     break;
	case C_POP: sp++; break;
	case C_EXIT: return mem[sp];
	case C_DUP: r = mem[sp]; sp--; mem[sp] = r; break;
	case C_DEREF: mem[sp] = mem[mem[sp]]; break;
	case C_POSTINC: mem[sp] = mem[mem[sp]]++; break;
	case C_POSTDEC: mem[sp] = mem[mem[sp]]--; break;
        default:
	  {
	    lit = opc % LITMOD;
	    opc = opc / LITMOD;
	    if (lit == LITMAX) {
		lit = mem[pc++];
		lit = lit+mem[pc++]*256;
	    } else if (LITMAX < lit)
		lit = lit + mem[pc++]*LITMUL - 1;
	    switch(opc) {
	    case X_PUSHAA: sp--; mem[sp] = fp+lit; break;
	    case X_PUSHAL: sp--; mem[sp] = fp-lit-FRAMESIZE-1; break;
	    case X_PUSHA: sp--; mem[sp] =  mem[fp+lit]; break;
	    case X_PUSHL: sp--; mem[sp] =  mem[fp-lit-FRAMESIZE-1]; break;
	    case X_PUSHAG: sp--; mem[sp] =  codesize+lit; break;
	    case X_PUSHG: sp--; mem[sp] = mem[codesize+lit]; break;
	    case X_PUSHAC: sp--; mem[sp] = lit; break;
	    case X_PUSHC: sp--; mem[sp] = lit; break;
	    case X_PUSHS: sp--; mem[sp] = pc; pc = pc + lit; break;
	    case X_ALLOC: sp = sp - lit; break;
	    case X_CALL:
	      r = mem[sp - -lit/2];
	      if (r < NPRECALL+1) {
		switch(r) {
		case 0: r = getchar(); break;
		case 1: r = putchar(mem[sp]); break;
		case 2: r = eputchar(mem[sp]); break;
		case 3: exit(mem[sp]); break;
		}
		sp = sp + lit/2;mem[sp] = r;if (lit%2) sp++ /* do return */;
	      }
	      else {
		mem[sp - F_PC] = pc;
		mem[sp - F_FP] = fp;
		mem[sp - F_NA] = lit;
		fp = sp;
		sp = sp - FRAMESIZE - DEFSTK;
		pc = r;
	      }
	    break;
	    case X_JUMP: pc = lit; break;
	    case X_TRUE: if (mem[sp++]) pc = lit; break;
	    case X_JFALSE: if (!mem[sp++]) pc = lit; break;
	    default:
	    fprintf(stderr, "unimpl op %d at %d\n", mem[oldpc], oldpc);
	    exit(1);
	    }
	  }
	}
    }
}
