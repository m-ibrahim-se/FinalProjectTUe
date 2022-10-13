import logging
logging.basicConfig(filename='example.log', level=logging.DEBUG)

import fitz
from os import listdir
from os.path import isfile, join
from conf import *
from custom_functions import *


def extractText(file):
    # returns text per page from the pdf doc
    doc = fitz.open(file)
    text = []
    for page in doc:
        t = page.getText().encode("utf8")
        text.append(t)

    return text


def getTocPageList(fp):
    # returns the Table of Contents and the list with "at what page does the section start" as two separate lists.
    logging.info('getTocPageList: started for file %s'%fp)
    doc = fitz.open(fp)

    toc = doc.get_toc()
    page_list = list()
    for [level, entry, page] in toc:
        page_list.append(page)
        logging.debug('TOC Entry: %s, page: %s\n'%(entry, page))
    logging.info('extracted TOC from document %s with %d entries'%(fp, len(page_list)))
    return page_list, toc


def extractSectionsFromPDF(text, toc, tocPageList, fp):
    docParts = list()
    if len(toc)!=0:
        for k in range(len(tocPageList)-1):
            docPart = "  "
            for l in range(tocPageList[k-1],tocPageList[k]):
                text_to_add = text[l]
                docPart = docPart + str(text_to_add)
            entry = dict()
            entry['title'] = toc[k]
            entry['text'] = docPart
            docParts.append(entry)
        docPart = "  "
        for l in range(tocPageList[-1], len(text)-1):
            text_to_add = text[l]
            docPart = docPart + str(text_to_add)
        entry = dict()
        entry['title'] = toc[-1]
        entry['text'] = docPart
        docParts.append(entry)
    else:
        entry = dict()
        entry['title'] = str(fp)
        docPart = '  '
        for page in text:
            docPart = docPart + str(page)
        entry['text'] = docPart
        docParts.append(entry)
    return docParts


def main():
    pdflist = retrieve_list_of_file_names(pdf_path)
    docs = list()
    for pdf in pdflist:
        text = extractText(join(pdf_path, pdf))
        tocPageList, toc = getTocPageList(join(pdf_path, pdf))
        docParts = extractSectionsFromPDF(text, toc, tocPageList, pdf)
        entry = dict()
        entry['doctitle'] = pdf
        entry['contents'] = docParts
        docs.append(entry)
    return docs

if __name__ == '__main__':
    docs = main()
    dump_retrieved_information(pdf_path, 'pdfs_parsed', docs)