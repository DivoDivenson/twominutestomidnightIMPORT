FILE * open_file(char filename[]){
   FILE * file;

   file = fopen(filename,"w");
   if(file == NULL){
     fprintf(stderr,"Error opening file %s\n",filename);
     exit(1);
   }
   return file;
}

//Oh good god
//Also, what is the difference between X and C instructions
void
dumpcode(int from, int to, FILE * file)
{
    int n, opc, oopc, lit, ofrom;
    char buf[200], *p;

    while(from < to) {
	ofrom = from;
	sprintf(buf, "/* %-4d:         ", from);
	p = buf + strlen(buf);
	oopc = opc = code[from++] & 0xff; //0xff sets all the bits in a char
                                     //ff = 8 * 1 etc
	if (opc >= LITLIM) {
	    lit = opc%LITMOD;
	    opc = opc-lit;
	    if (lit == LITMAX) {
		lit = (code[from]&0xff) + (code[from+1]&0xff)*256;
		from += 2;
	    } else if (lit > LITMAX) {
		lit = lit + (code[from++]&0xff)*LITMUL-1;
	    }
	}
	switch(opc) {
	case C_NOT: sprintf(p, "NOT */ mem[sp] = ! mem[sp];"); break;
	case C_NEG: sprintf(p, "NEG */ mem[sp] = - mem[sp];"); break;
	case C_ADD: sprintf(p, "ADD */ mem[sp+1] = mem[sp+1] + mem[sp]; sp++;"); break;
	case C_SUB: sprintf(p, "SUB */ mem[sp+1] = mem[sp+1] - mem[sp]; sp++;"); break;
	case C_DIV: sprintf(p, "DIV */ mem[sp+1] = mem[sp+1] / mem[sp]; sp++;"); break;
	case C_MUL: sprintf(p, "MUL */ mem[sp+1] = mem[sp+1] * mem[sp]; sp++;"); break;
	case C_MOD: sprintf(p, "MOD */ mem[sp+1] = mem[sp+1] % mem[sp]; sp++;"); break;
	case C_EQ: sprintf(p, "EQ */ mem[sp+1] = mem[sp+1] == mem[sp]; sp++;"); break;
	case C_NE: sprintf(p, "NE */ mem[sp+1] = mem[sp+1] != mem[sp]; sp++;"); break;
	case C_LT: sprintf(p, "LT */ mem[sp+1] = mem[sp+1] < mem[sp]; sp++;"); break;
	case C_GT: sprintf(p, "GT */ mem[sp+1] = mem[sp+1] > mem[sp]; sp++;"); break;
	case C_LE: sprintf(p, "LE */ mem[sp+1] = mem[sp+1] <= mem[sp]; sp++;"); break;
	case C_GE: sprintf(p, "GE */ mem[sp+1] = mem[sp+1] >= mem[sp]; sp++;"); break;
	case C_AND: sprintf(p, "AND */ mem[sp+1] = mem[sp+1] & mem[sp]; sp++;"); break;
	case C_OR: sprintf(p, "OR */ mem[sp+1] = mem[sp+1] | mem[sp]; sp++;"); break;
	case C_DUP: sprintf(p, "DUP */ r = mem[sp]; sp--; mem[sp] = r;"); break;
	case C_POSTINC: sprintf(p, "POSTINC */ mem[sp] = mem[mem[sp]]++;"); break;
	case C_POSTDEC: sprintf(p, "POSTDEC */ mem[sp] = mem[mem[sp]]--;"); break;
	case C_ASSIGN: sprintf(p, "ASSIGN */ r = mem[sp++]; mem[mem[sp]] = r; mem[sp] = r;"); break;
	case C_ASSIGNPOP: sprintf(p, "ASSIGNPOP */ r = mem[sp++]; mem[mem[sp]] = r; sp++;"); break;
	case C_RETURN: sprintf(p, "RETURN  Hey there now, lets do this one later ok*/"); break;
	case C_POP: sprintf(p, "POP */ sp++;"); break;
	case C_DEREF: sprintf(p, "DEREF */ mem[sp] = mem[mem[sp]];"); break;
	case C_JUMP: sprintf(p, "JUMP %d */ pc = lit;", lit); break;
	case C_JFALSE: sprintf(p, "JFALSE %d */ if (!mem[sp++]) pc = lit;", lit); break;
	case C_TRUE: sprintf(p, "JTRUE %d */ if (mem[sp++]) pc = lit;", lit); break;

//All these implement the X version of the instruction, not the C
	case C_PUSHAL: sprintf(p, "PUSHAL %d */ sp--; mem[sp] = fp-lit-FRAMESIZE-1;", lit); break;
	case C_PUSHL: sprintf(p, "PUSHL %d */ sp--; mem[sp] =  mem[fp-lit-FRAMESIZE-1];", lit); break;
	case C_PUSHAA: sprintf(p, "PUSHAA %d */ sp--; mem[sp] = fp+lit;", lit); break;
	case C_PUSHA: sprintf(p, "PUSHA %d */ sp--; mem[sp] =  mem[fp+lit];", lit); break;
	case C_PUSHAG: sprintf(p, "PUSHAG %d */ sp--; mem[sp] =  codesize+lit;", lit); break;
	case C_PUSHG: sprintf(p, "PUSHG %d */ sp--; mem[sp] = mem[codesize+lit];", lit); break;
	case C_PUSHC: sprintf(p, "PUSHC %d */ sp--; mem[sp] = lit;", lit); break;
	case C_PUSHAC: sprintf(p, "PUSHAC %d */ sp--; mem[sp] = lit;", lit); break;
	case C_PUSHS: 
	    sprintf(p, "PUSHS %d \"", lit);
	    n = lit;
	    p += strlen(p);
	    while(n--) *p++ = code[from++] ? code[from-1] : '' ;
	    strcpy(p, "\" */ sp--; mem[sp] = pc; pc = pc + lit;");
	    break;
	case C_CALL: sprintf(p, "CALL %d  Leave this for later */", lit); break;
	case C_ALLOC: sprintf(p, "ALLOC %d */ sp = sp - lit;", lit); break;
  case C_EXIT: sprintf(p, "EXIT */ return mem[sp];\n}"); break; //Insert final brace here to finish program
	default: sprintf(p, "unknown %d", oopc);
	}
   //sprintf(p,"*/");
	p = buf + 7;
	n = 0;
	while(ofrom < from && n < 3) {
	    sprintf(p, "%02x", code[ofrom++] & 0xff);
	    p += 2;
	    n++;
	}
	*p = ' ';
	fprintf(file, "%s\n", buf);
    }
}

#ifdef DISAS
main(){
    int codesize, a;
    int *p;
    getchar();
    getchar();
    codesize = getchar();
    codesize = codesize + getchar()*256;
    a = codesize;
    p = code;
    while(a--)
	*p++ = getchar();
    dumpcode(0, codesize);
    //exit(0);
}
#endif
