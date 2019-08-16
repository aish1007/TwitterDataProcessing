text_file = sc.textFile("/home/ubuntu/server/searchTweets.txt")
words = ['safe', 'not safe','pollution','accident','long waiting','expensive','friendly','snow strom','good school','bad school','poor school','immigrants','buses','park','parking']
word_count2 = text_file.flatMap(lambda line: line.split(" ")).filter(lambda w:w.lower() in words).map(lambda word:(word, 1)).reduceByKey(lambda a, b: a + b).map(lambda (a, b): (b, a)).sortByKey(1, 1)
word_count2.saveAsTextFile("/home/ubuntu/server/searchSpecificWords.txt")