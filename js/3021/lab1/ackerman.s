//x86_64 

.file	"ackerman.c"
.globl count // int count = 0;
	.bss
	.align 4
	.type	count, @object
	.size	count, 4
count:
	.zero	4
	.text
.globl ackerman //method label
	.type	ackerman, @function
ackerman:
.LFB2:
	pushq	%rbp  //Save address to return too
.LCFI0:
	movq	%rsp, %rbp
.LCFI1:
	subq	$16, %rsp
.LCFI2:
	movl	%edi, -4(%rbp) //Method params,x
	movl	%esi, -8(%rbp) //y
	movl	count(%rip), %eax //load count
	addl	$1, %eax //increment count
	movl	%eax, count(%rip) //store count
	cmpl	$0, -4(%rbp) // if (x == 0)
	jne	.L2
	movl	-8(%rbp), %eax //load y
	addl	$1, %eax //increment y
	movl	%eax, -12(%rbp) //store y
	jmp	.L4 //return
.L2: //else if
	cmpl	$0, -8(%rbp)//if  (y == 0)
	jne	.L5
	movl	-4(%rbp), %edi //Setup method params, x
	subl	$1, %edi //sub 1 from x
	movl	$1, %esi //second param, 1
	call	ackerman //recursive call
	movl	%eax, -12(%rbp)
	jmp	.L4 //return
.L5: //else
	movl	-8(%rbp), %esi //Setup method params, load x
	subl	$1, %esi //sub 1 from x
	movl	-4(%rbp), %edi //store x-1 as first param
	call	ackerman //recursive call
	movl	%eax, %esi //Setup method params for nested call, x
	movl	-4(%rbp), %edi //load y
	subl	$1, %edi //syb 1 from y
	call	ackerman //nested recrusive call
	movl	%eax, -12(%rbp)
.L4: //return
	movl	-12(%rbp), %eax
	leave
	ret
.LFE2:
	.size	ackerman, .-ackerman
	.section	.rodata
.LC0:
	.string	"Called %d times\n" //printf
	.text
.globl main
	.type	main, @function
main:
.LFB3:
	pushq	%rbp
.LCFI3:
	movq	%rsp, %rbp
.LCFI4:
	subq	$16, %rsp
.LCFI5: //main
	movl	$6, %esi //arguments to acmerkman call in main
	movl	$3, %edi
	call	ackerman
	movl	%eax, -4(%rbp) //store result in r
	movl	count(%rip), %esi
	movl	$.LC0, %edi
	movl	$0, %eax
	call	printf
	movl	$0, %eax
	leave
	ret
.LFE3:
	.size	main, .-main
	.section	.eh_frame,"a",@progbits
.Lframe1:
	.long	.LECIE1-.LSCIE1
.LSCIE1:
	.long	0x0
	.byte	0x1
	.string	"zR"
	.uleb128 0x1
	.sleb128 -8
	.byte	0x10
	.uleb128 0x1
	.byte	0x3
	.byte	0xc
	.uleb128 0x7
	.uleb128 0x8
	.byte	0x90
	.uleb128 0x1
	.align 8
.LECIE1:
.LSFDE1:
	.long	.LEFDE1-.LASFDE1
.LASFDE1:
	.long	.LASFDE1-.Lframe1
	.long	.LFB2
	.long	.LFE2-.LFB2
	.uleb128 0x0
	.byte	0x4
	.long	.LCFI0-.LFB2
	.byte	0xe
	.uleb128 0x10
	.byte	0x86
	.uleb128 0x2
	.byte	0x4
	.long	.LCFI1-.LCFI0
	.byte	0xd
	.uleb128 0x6
	.align 8
.LEFDE1:
.LSFDE3:
	.long	.LEFDE3-.LASFDE3
.LASFDE3:
	.long	.LASFDE3-.Lframe1
	.long	.LFB3
	.long	.LFE3-.LFB3
	.uleb128 0x0
	.byte	0x4
	.long	.LCFI3-.LFB3
	.byte	0xe
	.uleb128 0x10
	.byte	0x86
	.uleb128 0x2
	.byte	0x4
	.long	.LCFI4-.LCFI3
	.byte	0xd
	.uleb128 0x6
	.align 8
.LEFDE3:
	.ident	"GCC: (GNU) 4.1.3 20080704 (prerelease) (Debian 4.1.2-25)"
	.section	.note.GNU-stack,"",@progbits
