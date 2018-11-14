'''from subprocess import Popen, PIPE, STDOUT
import io
p = Popen(['java', '-jar', '.\\out\\artifacts\\ATSP_jar\\ATSP.jar','test1','greedy','general'], stdout=PIPE, stderr=STDOUT)
for line in io.TextIOWrapper(p.stdout, encoding="utf-8"):
    print(line)'''
import subprocess
import os
import json
import math
import statistics
import matplotlib.pyplot as plt
from itertools import cycle
import re
import numpy as np

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
    for al in ["greedy","steepest","simpeheuristic"]:
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

def createBestsFile(algorithms=["greedy","steepest","simpeheuristic","random"]):
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

def calculateQualityAndDeviation(algorithms=["greedy","steepest","simpeheuristic","random"]):
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
                writer.write(b[0]+";inf;inf;0;"+time+"\n")
            else:
                bestResult=float(data[1][1])/theBest-1
                avgResult=float(data[1][2])/theBest-1
                deviation=statistics.stdev([float(x[1])/theBest-1 for x in data[2:]])
                writer.write(b[0]+";"+str(bestResult)+";"+str(avgResult)+";"+str(deviation)+";"+time+"\n")
        writer.close()

#calculateQualityAndDeviation()#(solution-optimum)/optimum im mniejsze tym lepsze 0==optimum

def createQualityPlot(algorithms=["greedy","steepest","simpeheuristic","random"],type="best"):
    cycol = cycle('brgcmk')
    marker = "s"
    fig, ax = plt.subplots()
    bar_width=0.2
    data=1
    if(type=="time"):
        data=4
    elif(type=="avg"):
        data=2
    i=0
    for algo in algorithms:
        f = open(".\\solutions\\"+algo+"\\Quality.txt", "r")
        quality=[b.strip().split(";") for b in f.readlines()]
        quality=sorted(quality,key=lambda l:int(re.sub("[^0-9]", "",l[0])))
        f.close()
        x=[x[0] for x in quality]
        y=[float(x[data]) for x in quality]
        index = np.arange(len(x))
        if data==2:
            dev=[float(x[3]) for x in quality]
            ax.errorbar(index+bar_width*i,y,dev,linestyle='None', color=next(cycol),marker=marker,label=algo)
        else:
            ax.plot(index+bar_width*i,y, next(cycol)+marker,label=algo)
        i+=1
    ax.set_xticks(index + bar_width*i / 2)
    ax.set_xticklabels(x)
    ax.legend()
    fig.tight_layout()
    plt.show()

#albo skala logarytmiczna albo podzielic wykresy
#createQualityPlot(type="best")
#createQualityPlot(type="avg")
#createQualityPlot(type="time")

def runMultiForG():
    runJar('atspmultirun',"greedy","multi","300")
    for name in [x for x in os.listdir(".\\solutions\\greedy") if x.startswith("multi_300_")]:
        file = open(".\\solutions\\greedy\\"+name, "r")
        SummaryLine=file.readlines()[301].strip()
        file.close()
        updateBestsFile(name[6:-4],SummaryLine)
#runMultiForG()

def createQualityStartAndEndPlot():
    cycol = cycle('brgcmk')
    marker = "s"
    f = open(".\\solutions\\bests.txt", "r")
    bests=[b.strip().split(";") for b in f.readlines()]
    f.close()
    for nameOfFile in [x for x in os.listdir(".\\solutions\\greedy") if x.startswith("multi_300_")]:
        fig, ax = plt.subplots()
        theBest=[float(b[1]) for b in bests if b[0]==nameOfFile[10:-4]][0]
        file = open(".\\solutions\\greedy\\"+nameOfFile, "r")
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
#createQualityStartAndEndPlot()

def createQualityToRunNumberPlot():
    cycol = cycle('brgcmk')
    marker = "."
    f = open(".\\solutions\\bests.txt", "r")
    bests=[b.strip().split(";") for b in f.readlines()]
    f.close()
    for nameOfFile in [x for x in os.listdir(".\\solutions\\greedy") if x.startswith("multi_300_")]:
        fig, ax = plt.subplots()
        theBest=[float(b[1]) for b in bests if b[0]==nameOfFile[10:-4]][0]
        file = open(".\\solutions\\greedy\\"+nameOfFile, "r")
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
#createQualityToRunNumberPlot()
'''
def createPermutationSimilarityPlot():
    cycol = cycle('brgcmk')
    marker = "."
    f = open(".\\solutions\\bests.txt", "r")
    bests=[b.strip().split(";") for b in f.readlines()]
    f.close()
    for nameOfFile in [x for x in os.listdir(".\\solutions\\greedy") if x.startswith("multi_300_")]:
        fig, ax = plt.subplots()
        theBest=[float(b[1]) for b in bests if b[0]==nameOfFile[10:-4]][0]
        file = open(".\\solutions\\greedy\\"+nameOfFile, "r")
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
'''
#TODO jakosc w czasie jakosc/czas ?
#TODO liczba krokow/ocen trzeba wyciagnac i zrobic wykresy 
#TODO podobienstwo samych permutacji