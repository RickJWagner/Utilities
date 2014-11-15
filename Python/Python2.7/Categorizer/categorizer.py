from collections import OrderedDict

CATEGORIES = "BPEL, CAMEL, CLUSTER, CONFIGURATION, DEPLOYER, DEPLOYMENT, DOCUMENTATION, DTGOV, GARBAGE COLLECTION, GC, GOVERNANCE, JMX, JMS, RTGOV, EAP, HTTP, HTTPS, INSTALLER, INSTALLATION, MAVEN, HORNETQ, MESSAGING, RULES, SECURITY, DROOLS, JBPM, REST, RESTEASY, SWITCHYARD, TRANSFORMATION, TRANSFORMER, SOAP, JBDS, TOOLING, WSDL, CXF, VALIDATION, WEB SERVICE"


def first_line_of_new_case(line):
    if "RED HAT JBOSS FUSE SERVICE WORKS" in line:
        return True
    else:
        return False

def get_casenum(line):
    return line[36:44]


filein =  open('SFCases.csv', 'r')
casenum = 0
dictionary_of_cases = {}
dictionary_of_categories = {}
categories = CATEGORIES.split(',')

for category in categories:
    dictionary_of_categories[category] = []
    

for rawline in filein:
    line = rawline.upper()  
    if first_line_of_new_case(line):
        casenum = get_casenum(line)
        dictionary_of_cases[casenum] = set()
    
    
    for category in categories:
        #print "Looking for " + category + " in " + line
        if category in line:
            # track the categories for this case
            case_categories = dictionary_of_cases[casenum]
            case_categories.add(category)
            #dictionary_of_cases[casenum] = case_categories
            # track the cases for each category
            cases_so_far =     dictionary_of_categories[category]
            cases_so_far.append(casenum)
            dictionary_of_categories[category] = cases_so_far
        
# print results

print categories

print "\n\n\n"


# tally and sort the counts and categories
count_category = {}
for cat in dictionary_of_categories.keys():
    cases = list(dictionary_of_categories[cat])
    count_and_category = str(len(cases)) + " " + cat
    #print count_and_category
    count_category[len(cases)] = cat

for count in count_category.keys():
    print str(count) + " " + count_category[count]


#print count_category
    
print "\n\n\n"

for cat in dictionary_of_categories.keys():
    cases = list(dictionary_of_categories[cat])
    print cat + " " + str(len(cases))
    print cases
    print " \n\n"



for case in dictionary_of_cases.keys():
    categories = list(dictionary_of_cases[case])
    print case
    if not categories:
        print "NO CATEGORIES!"
    else:
        print categories
    print "\n\n\n"
