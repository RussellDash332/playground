"""
Words Searching
"""
from __future__ import print_function, division
import random
import string
def process_file(filename, skip_header):
    hist = {}
    fp = open(filename)
    if skip_header:
        skip_gutenberg_header(fp)
    for line in fp:
        process_line(line, hist)
    return hist

def skip_gutenberg_header(fp):
    for line in fp:
        if line.startswith('*END*THE SMALL PRINT!'):
            break

def process_line(line, hist):
    line = line.replace('-', ' ')
    strippables = string.punctuation + string.whitespace
    for word in line.split():
        word = word.strip(strippables)
        word = word.lower()
        hist[word] = hist.get(word, 0) + 1

def most_common(hist):
    t = []
    for key, value in hist.items():
        t.append((value, key))
    t.sort()
    t.reverse()
    return t

def print_most_common(hist, num=10):
    t = most_common(hist)
    print('The most common words are:')
    for freq, word in t[:num]:
        print(word, '\t', freq)

def subtract(d1, d2):
    res = {}
    for key in d1:
        if key not in d2:
            res[key] = None
    return res

def total_words(hist):
    return sum(hist.values())

def different_words(hist):
    return len(hist)

def random_word(hist):
    t = []
    for word, freq in hist.items():
        t.extend([word] * freq)
    return random.choice(t)

def main():
    hist = process_file('emma.txt', skip_header=True)
    print('Total number of words:', total_words(hist))
    print('Number of different words:', different_words(hist))
    t = most_common(hist)
    print('The most common words are:')
    for freq, word in t[0:20]:
        print(word, '\t', freq)
    words = process_file('dictionary.txt', skip_header=False)
    diff = subtract(hist, words)
    print("The words in the book that aren't in the word list are:")
    for word in diff.keys():
        print(word, end=' ')
    print("\n\nHere are some random words from the book")
    for i in range(100):
        print(random_word(hist), end=' ')

if __name__ == '__main__':
    main()