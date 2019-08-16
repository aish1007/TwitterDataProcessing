# TwitterDataProcessing
Using Apache Spark
### Virtual System Set-Up 

1. Created cloud developer account using AMAZON AWS and created EC2 instance.
2. Connected to EC2 instance using PUTTYGEN for creating private keys and PUTTY to connect with the instance.

 3. Apache Spark Installation: - 
   - Installed JDK and PYTHON in terminal 
   -  Installed Apache Spark and initialized the master and slave. 
   - Started the spark session by using the command sudo ./spark-2.4.0-bin-hadoop2.7/bin/pyspark
 
 ### Data Extraction
- Created twitter developer account and generated the keys required for authentication. 
- Used java programming for twitter stream API and search API extraction with the key word “Halifax” and extracted 1000 data points for both stream API and search API. I used HOSEBIRD library for extraction of stream API and okHttp client for search API. Extracted tweets, retweets, metadata etc and stored the data in MONGODB.
- Extracted data was cleaned with removal of special characters, stop words, punctuation, urls and emoticons using java regex pattern. The cleaned file was then uploaded to CLOUD in txt format.

### Map Reducer 
- MAP REDUCER program was developed with python spark to count the over all words in a tweet and to count the list of specific words.
- The words are sorted according to their occurence in ascending order to find out the word with highest occurence. 



