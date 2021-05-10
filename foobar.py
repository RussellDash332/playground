def foo(n):
	if n == 0:
		return 0
	else:
		return n + foo(n-1)
		
def bar(n):
	if n == 0:
		return 0
	else:
		return foo(n) + bar(n-1)
			
def improved_foo(n):
	return n*(n+1)*(n+2)//6
	
print(foo(10))
print(bar(10))
print(improved_foo(10))