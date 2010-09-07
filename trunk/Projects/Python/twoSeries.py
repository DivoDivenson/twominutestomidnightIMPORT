def fx(n):
   if(n != 0):
      return (n - mx(fx(n - 1)))

   return 1


def mx(n):
   if(n != 0):
      return (n - fx(mx(n - 1)))

   return 0


for x in range (1, 60):
   print repr(fx(x)), repr(mx(x))
