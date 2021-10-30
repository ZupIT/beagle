'''
This script imports one or more phased or unphased genotype
output files from BEAGLE.

Author: Greta Linse Peterson, Golden Helix, Inc.
Last Revised: 2013-12-27
'''
ghi.requireVersion('7.4')
import os
import gzip
import struct

def scan_files(paths):
    
    all_samps = set()
    for filepath in paths:
        if filepath.endswith('.gz'):
            f = gzip.open(filepath,'r')
        else:
            f = open(filepath, "r")       
        line = f.readline().rstrip()
        v = line.split()    

        if v[0] != "I":
            ghi.message("Sample names must be in the first row of each file.")
            return None
            
        sampleid = v[2::2]  
        all_samps.update(sampleid)
        f.close()
    all_samps = list(all_samps)
    
    rowDict = {sample:i for i,sample in enumerate(all_samps)}
    
    return rowDict,all_samps



def import_beagle(name,paths):
    
    result = scan_files(paths)    
    if not result:
        return
    rowDict,all_samps = result    
    
    myBuilder = ghi.dataSetBuilder(name, len(all_samps))
    myBuilder.addRowLabels('Sample', all_samps)

    phenotype = {}
    
    progress = ghi.progressDialog('Importing BEAGLE files...',10000)
    progress.setDoubleProgressMode(0,10000,'Importing BEAGLE files...')
    progress.show()
    count = 0

    for pcount,filepath in enumerate(paths):
        if filepath.endswith('.gz'):
            f = gzip.open(filepath,'r')
            ftemp = open(filepath,'rb')
            ftemp.seek(-4,2)
            fs = int(abs(struct.unpack("<i",ftemp.read())[0]))

        else:
            f = open(filepath, "r") 
            fileStats = os.stat(filepath)
            fs = int(fileStats.st_size)
        progress.setSecondaryMessage("Reading " + os.path.basename(filepath))
        progress.setSecondaryTotalSteps(10000)
        count = 1        
        
        genotype = [None for samp in range(len(all_samps))]

        line = f.readline().rstrip(' \r\n')
        v = line.split()

        currentSamples = [str(v[i]) for i in range(2,len(v),2)]
                
        while 1:
            line = f.readline().rstrip(' \r\n')

            if not line: 
                break
            
            if int(10000.0*f.tell()/fs) > count:
                count = int(10000.0*f.tell()/fs)
                progress.setSecondaryProgress(count)
            if progress.wasCanceled():
                progress.setSecondaryProgress(10000)
                progress.setProgress(10000) #make sure the progress bar completes
                f.close()
                return
            
            v = line.split()
            label = v[0]
            markerName = v[1]
            v = v[2:len(v)]
            
            if label == "M":
                for i,sample in zip(range(0,len(v),2),currentSamples):
                    sIdx = rowDict[sample]
                    allele1 = str(v[i])
                    allele2 = str(v[i+1])
                    if allele1 > allele2:
                        genotype[sIdx] = allele2 + "_" + allele1
                    else:
                        genotype[sIdx] = allele1 + "_" + allele2
                myBuilder.addGeneticColumn(markerName, genotype)
                
            else:
                col = v[::2]
                if not phenotype.has_key(markerName):
                    phenotype[markerName] = col
        
        progress.setSecondaryProgress(10000)            
        progress.setProgress(pcount+1)
        f.close()
    
    log = 'Imported BEAGLE output files from \n' + '\n'.join(paths)    
        
    if phenotype:
        phenoBuilder = ghi.dataSetBuilder(name + ' - Phenotypes',len(all_samps))
        phenoBuilder.addRowLabels('Sample',all_samps)
        for header,column in phenotype.iteritems():
            phenoBuilder.addColumn(header,column)
        pheno = phenoBuilder.finish()
        pheno.appendLog(log)
    newSS = myBuilder.finish()
    
    newSS.appendLog(log)
    
    joinedSS = pheno.joinByRowLabels(newSS.nodeID())
    joinedSS.show()

def prompt():
    pdl = [{'name':'name','type':'string','label':'Dataset Name:','default':'BEAGLE Files'},
            {'name':'paths','type':'files','label':"Choose the BEAGLE output files to import...",'filter':"Phased *.phased;;Beagle *.bgl;;Compressed *.gz;;All Files *.*"}]
    prompt = ghi.promptDialog(pdl,title = 'Import BEAGLE from One or More Files')
    if not prompt:
        return
    
    name = prompt['name']
    paths = prompt['paths']
    
    import_beagle(name,paths)
    

try:
    prompt()
        
except:
    ghi.error()
