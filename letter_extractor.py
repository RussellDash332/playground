words = ['ALIBABA','A QUICK BROWN FOX JUMPS OVER THE LAZY DOG','I AM DOING CODING RIGHT NOW','THIS IS A TEST SENTENCE','HELLO']

for i in range(len(words)):    
    alphabet = {}
    alpha_list = []

    for j in range(len(words[i])):
        if words[i][j] != ' ':
            if words[i][j] in alphabet:
                alphabet[words[i][j]] += 1
            else:
                alphabet[words[i][j]] = 1
    letters = list(alphabet)
    
    print(words[i],end=' = ')
    print(", ".join(sorted(alphabet.keys())))
    
    for key,value in sorted(alphabet.items()):
        alpha_list.append("%s(%d)"%(key,value))
    print(", ".join(alpha_list))
    print()
