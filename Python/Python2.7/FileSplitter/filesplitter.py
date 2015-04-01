from collections import OrderedDict


def first_line_of_new_dump(line):
    if "2015-03-21" in line:
        return True
    else:
        return False


filein =  open('dumps.txt', 'r')
outcount = 0
fileout = open('ThreadDump' + str(outcount), 'w')

for line in filein:
    if first_line_of_new_dump(line):
        fileout.close()
        outcount = outcount + 1
        fileout = open('ThreadDump' + str(outcount), 'w')
    
    fileout.write(line)

filein.close()
fileout.close()

