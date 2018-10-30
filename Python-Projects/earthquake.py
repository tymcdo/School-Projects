# data from http://earthquake.usgs.gov/fdsnws/event/1/query?format=text&starttime=2016-01-01&endtime=2016-01-02
# this could be modified to get the data directly from the web instead of copying/pasting to text file
# you could even have this program modify the query for a certain date range, etc.
# import urllib.request # DEPRECATED
import urllib

page = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=text&starttime=2016-01-01&endtime=2016-01-02"
file = "earthquakeData.txt"

# Function to produce a list of earthquake magnitudes from the provided data source.
# parameter float threshold: the list will include only magnitudes of this value or higher
# parameter string source:  data source.  Can be a file or a URL
# return:  list of earthquake magnitudes that meet the criteria
def makeMagnitudeList(threshold,source):
    if source.lower().find("http://")==0:
        # it's a web page!
        # quakefile = urllib.request.urlopen(source) # DEPRECATED
        quakefile = urllib.urlretrieve(source)
        webpage = True
    else:
        quakefile = open(source,"r")
        webpage = False
    magHeading = "Magnitude"
    maglist = []
    headerFound = False
    for aline in quakefile:
        aline = aline[:-1]  # strip off the ending character '\n'
        if webpage:
            vlist = aline.decode("utf-8").split('|')  # column delimiter for text file
        else:
            vlist = aline.split('|')
        # First line of data file has column headings with same delimiter
        # Assume that if list has an item with value "Magnitude" then it's the header line
        if not headerFound:
            if vlist.count(magHeading) > 0:
                magIndex = vlist.index(magHeading)
                headerFound = True
        else:
            magnitude = float(vlist[magIndex])
            if magnitude >= threshold:
                maglist.append(magnitude)
    return maglist

