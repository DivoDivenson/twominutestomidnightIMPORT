;So eax is a general purpose calculation register
;
.file	"sort.c"
	.text
.globl insertionSort
	.type	insertionSort, @function
insertionSort:
.LFB2:
	pushq	%rbp
.LCFI0:
	movq	%rsp, %rbp
.LCFI1:
	movq	%rdi, -24(%rbp)
	movl	%esi, -28(%rbp)
	movl	$0, -4(%rbp)  ;insertionCount
	movl	$1, -16(%rbp) ;i, count in 'for' loop
	jmp	.L2
.L6:
	movl	-16(%rbp), %eax
	movl	%eax, -12(%rbp)
	movl	-12(%rbp), %eax
	cltq
	salq	$2, %rax
	addq	-24(%rbp), %rax
	movl	(%rax), %eax
	movl	%eax, -8(%rbp)
	jmp	.L3
.L5:
	movl	-12(%rbp), %eax
	cltq
	salq	$2, %rax
	movq	%rax, %rcx
	addq	-24(%rbp), %rcx
	movl	-12(%rbp), %eax
	movslq	%eax,%rdx
	movl	$1, %eax
	subq	%rdx, %rax
	leaq	0(,%rax,4), %rdx
	movq	%rdx, %rax
	salq	$62, %rax
	subq	%rdx, %rax
	addq	-24(%rbp), %rax
	movl	(%rax), %eax
	movl	%eax, (%rcx)
	subl	$1, -12(%rbp)
	addl	$1, -4(%rbp)
.L3:
	cmpl	$0, -12(%rbp)
	jle	.L4
	movl	-12(%rbp), %eax
	movslq	%eax,%rdx
	movl	$1, %eax
	subq	%rdx, %rax
	leaq	0(,%rax,4), %rdx
	movq	%rdx, %rax
	salq	$62, %rax
	subq	%rdx, %rax
	addq	-24(%rbp), %rax
	movl	(%rax), %eax
	cmpl	-8(%rbp), %eax
	jg	.L5
.L4:
	movl	-12(%rbp), %eax
	cltq
	salq	$2, %rax
	movq	%rax, %rdx
	addq	-24(%rbp), %rdx
	movl	-8(%rbp), %eax
	movl	%eax, (%rdx)
	addl	$1, -16(%rbp)
.L2:
	movl	-16(%rbp), %eax
	cmpl	-28(%rbp), %eax
	jl	.L6
	leave
	ret
.LFE2:
	.size	insertionSort, .-insertionSort
.globl main
	.type	main, @function
main:
.LFB3:
	pushq	%rbp
.LCFI2:
	movq	%rsp, %rbp
.LCFI3:
	subq	$48, %rsp
.LCFI4:
	movl	$12, -32(%rbp)
	movl	$4, -28(%rbp)
	movl	$5, -24(%rbp)
	movl	$3, -20(%rbp)
	movl	$18, -16(%rbp)
	movl	$65, -12(%rbp)
	movl	$12, -8(%rbp)
	leaq	-32(%rbp), %rdi
	movl	$7, %esi
	call	insertionSort
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
	.long	.LCFI2-.LFB3
	.byte	0xe
	.uleb128 0x10
	.byte	0x86
	.uleb128 0x2
	.byte	0x4
	.long	.LCFI3-.LCFI2
	.byte	0xd
	.uleb128 0x6
	.align 8
.LEFDE3:
	.ident	"GCC: (Debian 4.3.2-1.1) 4.3.2"
	.section	.note.GNU-stack,"",@progbits
