import subprocess
import os
import json
import math
import statistics
import matplotlib.pyplot as plt
import matplotlib
from itertools import cycle
import re
import numpy as np
from ast import literal_eval

def runJar(
fileName='test1',
algorithm="greedy",
solver="multi",
timeForRandom="",
timesRepeat="1"
):
    if timeForRandom=="":
        subprocess.call(['java', '-jar', '.\\out\\artifacts\\ATSP_jar\\ATSP.jar',fileName,algorithm,solver,timesRepeat])
    else:
        subprocess.call(['java', '-jar', '.\\out\\artifacts\\ATSP_jar\\ATSP.jar',fileName,algorithm,solver,timeForRandom,timesRepeat])

def runGeneralAndTimeForGSH():
    for al in ["greedy","steepest","simpleheuristic"]:
        for sol in ["general","time"]:
            runJar('atsp',al,sol)
#runGeneralAndTimeForGSH()


def runGeneralAndTimeForR():
    timeDic={}
    for name in [x for x in os.listdir(".\\solutions\\greedy") if x.startswith("time_")]:
        file1 = open(".\\solutions\\greedy\\"+name, "r") 
        v1=float(file1.readlines()[1])
        file1 = open(".\\solutions\\steepest\\"+name, "r") 
        v2=float(file1.readlines()[1])
        timeDic[name[5:-4]]=int((v1+v2)/2)
    jsonDic=str(json.dumps(timeDic)).replace(" ", "")
    print(jsonDic)
    for sol in ["general",'time']:
        runJar('atsp',"random",sol,jsonDic)
#runGeneralAndTimeForR()

def createBestsFile(algorithms=["greedy","steepest","simpleheuristic","random"]):
    f = open(".\\solutions\\bests.txt", "w")
    for name in [x for x in os.listdir(".\\solutions\\greedy") if x.startswith("general_")]:
        cost=math.inf
        solutionPerm=""
        for algo in algorithms:
            file = open(".\\solutions\\"+algo+"\\"+name, "r")
            SummaryLine=file.readlines()[1].split(';')
            file.close()
            c=SummaryLine[1]
            if c!='Infinity':
                if(float(c)<cost):
                    cost=float(c)
                    solutionPerm=SummaryLine[0]
        f.write(name[8:-4]+";"+str(cost)+";"+solutionPerm+"\n")
    f.close()
#createBestsFile()

def updateBestsFile(name="",summaryLineToUpdate="Infinity;"):
    data=summaryLineToUpdate.split(';')
    if data[0]!='Infinity':
        f = open(".\\solutions\\bests.txt", "r")
        bests=f.readlines()
        f.close()
        i=0
        for b in bests:
            bestData=b.split(';')
            if(bestData[0]==name and float(bestData[1])>float(data[0])):
                bests[i]=name+';'+summaryLineToUpdate+"\n"
                f = open(".\\solutions\\bests.txt", "w")
                for b in bests:
                   f.write(b)
                f.close()
                break
            i+=1
#updateBestsFile("rbg403","150.0;[1, 2, 3]")

def runMulti(algorithm="greedy",folder='atspmulti2'):
    runJar(folder,algorithm,"multi","300")
    for name in [x for x in os.listdir(".\\solutions\\"+algorithm) if x.startswith("multi_300_")]:
        file = open(".\\solutions\\"+algorithm+"\\"+name, "r")
        SummaryLine=file.readlines()[301].strip()
        file.close()
        updateBestsFile(name[6:-4],SummaryLine)
#runMulti("greedy")
#runMulti("steepest")

def calculateQualityAndDeviation(algorithms=["greedy","steepest","simpleheuristic","random"]):
    f = open(".\\solutions\\bests.txt", "r")
    bests=[b.strip().split(";") for b in f.readlines()]
    f.close()
    for algo in algorithms:
        writer = open(".\\solutions\\"+algo+"\\Quality.txt", "w")
        for b in bests:
            theBest=float(b[1])
            f = open(".\\solutions\\"+algo+"\\general_"+b[0]+".txt", "r")
            data=[b.strip().split(";") for b in f.readlines()] 
            f.close()
            f = open(".\\solutions\\"+algo+"\\time_"+b[0]+".txt", "r")
            time=f.readlines()[1].strip()
            f.close()
            if(float(data[1][1])==math.inf):
                writer.write(b[0]+";inf;inf;0;"+time+";"+data[1][4]+";"+data[1][5]+"\n")
            else:
                bestResult=float(data[1][1])/theBest-1
                avgResult=float(data[1][2])/theBest-1
                deviation=statistics.stdev([float(x[1])/theBest-1 for x in data[2:]])
                writer.write(b[0]+";"+str(bestResult)+";"+str(avgResult)+";"+str(deviation)+";"+time+";"+data[1][4]+";"+data[1][5]+"\n")
        writer.close()
#calculateQualityAndDeviation()

#2.1
#2.2
#2.4
#jakość - (solution-optimum)/optimum im mniejsze tym lepsze 0==optimum
def createQualityPlot(algorithms=["greedy","steepest","simpleheuristic","random"],type="best",fName="1.pdf",ylab="1"):
    cycol = "k" #w markerze cykl tu 1 kolor 144 146w166 jak    brgcmk
    marker = cycle('*s^opx')
    #marker = "s"
    fig, ax = plt.subplots()
    bar_width=0.2
    data=1
    if(type=="time"):
        data=4
    elif(type=="avg"):
        data=2
    elif(type=="iterations"):
        data=5
    elif(type=="evaluatedSolutions"):
        data=6
    i=0
    for algo in algorithms:
        f = open(".\\solutions\\"+algo+"\\Quality.txt", "r")
        quality=[b.strip().split(";") for b in f.readlines()]
        quality=sorted(quality,key=lambda l:int(re.sub("[^0-9]", "",l[0])))    #zeby przeciac wykres w polowie np
        f.close()
        x=[x[0] for x in quality]
        y=[float(x[data]) for x in quality]
        index = np.arange(len(x))
        if data==2:
            dev=[float(x[3]) for x in quality]
            ax.errorbar(index+bar_width*i,y,dev,linestyle='None', color=cycol,marker=next(marker),label=algo)
        else:
            ax.plot(index+bar_width*i,y, next(marker)+cycol,label=algo)
        i+=1
    ax.set_xticks(index + bar_width*i / 2)
    ax.set_xticklabels(x,rotation=50)
    ax.legend()
    fig.tight_layout()
    plt.xlabel("Nazwy instancji")
    plt.ylabel(ylab)
    #plt.show()
    plt.savefig(fName, bbox_inches='tight')

#albo skala logarytmiczna albo podzielic wykresy
#2.1
#createQualityPlot(type="best",fName="",ylab="")
#createQualityPlot(type="avg",fName="",ylab="")
#2.2
#createQualityPlot(type="time",fName="",ylab="")
#2.4
#createQualityPlot(algorithms=["greedy","steepest"],type="iterations",fName="",ylab="")
#createQualityPlot(algorithms=["greedy","steepest"],type="evaluatedSolutions",fName="",ylab="")

#2.3 jakosc/czas (jakosc/sekunde)
def createQualityInTimePlot(algorithms=["greedy","steepest","simpleheuristic","random"],best=True,avg=True):
    marker = cycle('*s^opx')
    fig, ax = plt.subplots()
    bar_width=0.2
    i=0
    for algo in algorithms:
        f = open(".\\solutions\\"+algo+"\\Quality.txt", "r")
        quality=[b.strip().split(";") for b in f.readlines()]
        quality=sorted(quality,key=lambda l:int(re.sub("[^0-9]", "",l[0])))
        f.close()
        x=[x[0] for x in quality]
        index = np.arange(len(x))
        m=next(marker)
        cm=['k'+m,'y'+m]
        if best:
            y1=[float(x[1])/float(x[4])*1000000000 for x in quality]
            ax.plot(index+bar_width*i,y1, cm[0],label=algo+" best")
        if avg:
            y2=[float(x[2])/float(x[4])*1000000000 for x in quality]
            ax.plot(index+bar_width*i,y2, cm[1],label=algo+" avg")
        i+=1
    ax.set_xticks(index + bar_width*i / 2)
    ax.set_xticklabels(x)
    ax.legend()
    fig.tight_layout()
    plt.show()
createQualityInTimePlot()
#createQualityInTimePlot(algorithms=["greedy","steepest"],best=False)
#createQualityInTimePlot(algorithms=["greedy","steepest"],avg=False)

#3
def createQualityStartAndEndPlot(algorithm="greedy"):
    cycol = cycle('brgcmk')
    marker = "s"
    f = open(".\\solutions\\bests.txt", "r")
    bests=[b.strip().split(";") for b in f.readlines()]
    f.close()
    for nameOfFile in [x for x in os.listdir(".\\solutions\\"+algorithm) if x.startswith("multi_300_")]:
        fig, ax = plt.subplots()
        theBest=[float(b[1]) for b in bests if b[0]==nameOfFile[10:-4]][0]
        file = open(".\\solutions\\"+algorithm+"\\"+nameOfFile, "r")
        data=file.readlines()
        file.close()
        name=data[0].strip()
        solutions=[d.strip().split(";") for d in data[1:301]]
        x=[float(x[0])/theBest-1 for x in solutions]
        y=[float(x[2])/theBest-1 for x in solutions]
        ax.plot(x,y, next(cycol)+marker,label=name)
        ax.legend()
        fig.tight_layout()
        plt.show()
#createQualityStartAndEndPlot("greedy")
#createQualityStartAndEndPlot("steepest")

#4
def createQualityToRunNumberPlot(algorithm="greedy"):
    cycol = cycle('brgcmk')
    marker = "."
    f = open(".\\solutions\\bests.txt", "r")
    bests=[b.strip().split(";") for b in f.readlines()]
    f.close()
    for nameOfFile in [x for x in os.listdir(".\\solutions\\"+algorithm) if x.startswith("multi_300_")]:
        fig, ax = plt.subplots()
        theBest=[float(b[1]) for b in bests if b[0]==nameOfFile[10:-4]][0]
        file = open(".\\solutions\\"+algorithm+"\\"+nameOfFile, "r")
        data=file.readlines()
        file.close()
        name=data[0].strip()
        solutions=[d.strip().split(";") for d in data[1:301]]
        x=range(1,301)
        v=[float(x[2])/theBest-1 for x in solutions]
        y=[min(v[:i]) for i in x]
        y2=[statistics.mean(v[:i]) for i in x]
        ax.plot(x,y, next(cycol)+marker,linestyle="--",label=name)
        ax.plot(x,y2, next(cycol)+marker,linestyle="--",label=name)
        ax.legend()
        fig.tight_layout()
        plt.show()
#createQualityToRunNumberPlot("greedy")
#createQualityToRunNumberPlot("steepest")

def pairsFromPermutation(perm):
    solArray=literal_eval(perm)
    solSize=len(solArray)
    return [str(solArray[i])+"-"+str(solArray[(i+1)%solSize]) for i in range(0,solSize)]
#5.1
def createPermutationSimilarityPlot(instance=['ftv33','p43'],algorithm="greedy"):
    for name in instance:
        file = open(".\\solutions\\"+algorithm+"\\multi_300_"+name+".txt", "r")
        data=file.readlines()
        file.close()
        solutions=[d.strip().split(";") for d in data[1:301]]
        solutions=sorted(solutions,key=lambda l:float(l[2]))
        solutionsPairs=[pairsFromPermutation(s[1]) for s in solutions]
        x=[]
        y=[]
        z=[]
        i=0
        for sol in solutionsPairs:
            j=0
            for s2 in solutionsPairs:
                same=0
                for pair in s2:
                    if pair in sol:
                        same+=1
                x.append(i)
                y.append(j)
                z.append(same/len(sol))
                j+=1
            i+=1
        cmap=plt.cm.gray
        norm = matplotlib.colors.BoundaryNorm(np.arange(0,1,0.01), cmap.N)
        plt.scatter(x,y,c=z,norm=norm,edgecolor='none',marker=',',cmap=cmap)
        plt.colorbar(ticks=np.linspace(0,1,11))
        plt.show()
#createPermutationSimilarityPlot(algorithm="greedy")
#createPermutationSimilarityPlot(algorithm="steepest")

#5.2
def createPermutationSimilarityToBestPlot(instance=['ftv33','p43'],algorithm="greedy"):
    f = open(".\\solutions\\bests.txt", "r")
    bests=[b.strip().split(";") for b in f.readlines()]
    f.close()
    for name in instance:
        theBest=[b for b in bests if b[0]==name][0]
        theBestV=float(theBest[1])
        theBestPairs=pairsFromPermutation(theBest[2])
        file = open(".\\solutions\\"+algorithm+"\\multi_300_"+name+".txt", "r")
        data=file.readlines()
        file.close()
        solutions=[d.strip().split(";") for d in data[1:301]]
        solutions=sorted(solutions,key=lambda l:float(l[2]))
        x=[float(s[2])/theBestV-1 for s in solutions]
        def permSim(pairs):
            same=0
            for pair in pairs:
                if pair in theBestPairs:
                    same+=1
            return same/len(theBestPairs)
        y=[permSim(pairsFromPermutation(s[1])) for s in solutions]
        fig, ax = plt.subplots()
        ax.plot(x,y, "k.")
        plt.show()

#createPermutationSimilarityToBestPlot(algorithm="greedy")
#createPermutationSimilarityToBestPlot(algorithm="steepest")