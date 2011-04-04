	.file	"bank.c"
.globl account_a
	.data
	.align 4
	.type	account_a, @object
	.size	account_a, 4
account_a:
	.long	100
.globl account_b
	.align 4
	.type	account_b, @object
	.size	account_b, 4
account_b:
	.long	100
.globl account_c
	.align 4
	.type	account_c, @object
	.size	account_c, 4
account_c:
	.long	100
	.text
.globl transfare
	.type	transfare, @function
transfare:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	pushq	%rbx
	subq	$56, %rsp
	movq	%rdi, -56(%rbp)
	movq	-56(%rbp), %rax
	movq	%rax, -48(%rbp)
	movq	-48(%rbp), %rax
	movq	(%rax), %rax
	movq	%rax, -40(%rbp)
	movq	-48(%rbp), %rax
	movq	8(%rax), %rax
	movq	%rax, -32(%rbp)
	movl	$0, -24(%rbp)
	jmp	.L2
	.cfi_offset 3, -24
.L5:
	call	rand
	movl	%eax, %ecx
	movl	$818089009, %edx
	movl	%ecx, %eax
	imull	%edx
	sarl	$2, %edx
	movl	%ecx, %eax
	sarl	$31, %eax
	movl	%edx, %ebx
	subl	%eax, %ebx
	movl	%ebx, %eax
	movl	%eax, -20(%rbp)
	movl	-20(%rbp), %edx
	movl	%edx, %eax
	sall	$2, %eax
	addl	%edx, %eax
	sall	$2, %eax
	addl	%edx, %eax
	movl	%ecx, %edx
	subl	%eax, %edx
	movl	%edx, %eax
	movl	%eax, -20(%rbp)
	movq	-40(%rbp), %rax
	movl	(%rax), %eax
	cmpl	-20(%rbp), %eax
	jge	.L3
	movq	-40(%rbp), %rax
	movl	(%rax), %eax
	movl	%eax, -20(%rbp)
.L3:
	cmpl	$0, -20(%rbp)
	jle	.L4
	movq	-40(%rbp), %rax
	movl	(%rax), %eax
	movl	%eax, %edx
	subl	-20(%rbp), %edx
	movq	-40(%rbp), %rax
	movl	%edx, (%rax)
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	movl	%eax, %edx
	addl	-20(%rbp), %edx
	movq	-32(%rbp), %rax
	movl	%edx, (%rax)
.L4:
	addl	$1, -24(%rbp)
.L2:
	cmpl	$999999, -24(%rbp)
	jle	.L5
	addq	$56, %rsp
	popq	%rbx
	leave
	ret
	.cfi_endproc
.LFE0:
	.size	transfare, .-transfare
	.section	.rodata
.LC0:
	.string	"a: %d, b: %d, c %d\n"
.LC1:
	.string	"Sum: %d\n"
	.text
.globl main
	.type	main, @function
main:
.LFB1:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	pushq	%rbx
	subq	$88, %rsp
	movl	$0, %edi
	.cfi_offset 3, -24
	call	time
	movl	%eax, %edi
	call	srand
	movq	$account_a, -64(%rbp)
	movq	$account_b, -56(%rbp)
	movq	$account_b, -80(%rbp)
	movq	$account_c, -72(%rbp)
	movq	$account_c, -96(%rbp)
	movq	$account_a, -88(%rbp)
	leaq	-64(%rbp), %rdx
	leaq	-48(%rbp), %rax
	movq	%rdx, %rcx
	movl	$transfare, %edx
	movl	$0, %esi
	movq	%rax, %rdi
	call	pthread_create
	leaq	-80(%rbp), %rax
	leaq	-48(%rbp), %rdx
	leaq	8(%rdx), %rbx
	movq	%rax, %rcx
	movl	$transfare, %edx
	movl	$0, %esi
	movq	%rbx, %rdi
	call	pthread_create
	leaq	-96(%rbp), %rax
	leaq	-48(%rbp), %rdx
	leaq	16(%rdx), %rbx
	movq	%rax, %rcx
	movl	$transfare, %edx
	movl	$0, %esi
	movq	%rbx, %rdi
	call	pthread_create
	movl	$0, -20(%rbp)
	jmp	.L8
.L9:
	movl	-20(%rbp), %eax
	cltq
	movq	-48(%rbp,%rax,8), %rax
	movl	$0, %esi
	movq	%rax, %rdi
	call	pthread_join
	addl	$1, -20(%rbp)
.L8:
	cmpl	$2, -20(%rbp)
	jle	.L9
	movl	account_c(%rip), %ecx
	movl	account_b(%rip), %edx
	movl	account_a(%rip), %ebx
	movl	$.LC0, %eax
	movl	%ebx, %esi
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf
	movl	account_a(%rip), %edx
	movl	account_b(%rip), %eax
	addl	%eax, %edx
	movl	account_c(%rip), %eax
	addl	%eax, %edx
	movl	$.LC1, %eax
	movl	%edx, %esi
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf
	movl	$0, %eax
	addq	$88, %rsp
	popq	%rbx
	leave
	ret
	.cfi_endproc
.LFE1:
	.size	main, .-main
	.ident	"GCC: (Debian 4.4.5-10) 4.4.5"
	.section	.note.GNU-stack,"",@progbits
