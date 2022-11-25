# Assignment 8 - Dataflow Testing (White-box Testing)

- Dataflow Coverage -> Considers how data gets accessed and modified in the system and how it could get corrupted.

Define:
- c-use
- p-use

// TODO -> descrever dataflow testing, c-use, p-use, all-defs, all-c-uses, all-p-uses, all-uses

## Dataflow Testing

### 1) `public static int parseSeconds(String strTime) throws ParseException`

```java 
1. public static int parseSeconds(String strTime) throws ParseException {
2.     Pattern p = Pattern.compile("(\\d+):([0-5]?\\d):([0-5]?\\d)");    // 0:00:00
3.     Matcher m = p.matcher(strTime);
4.
5.     if (!m.matches()) {
6.         throw new ParseException("Invalid seconds-string", 0);
7.     }
8.
9.     int hours = Integer.parseInt(m.group(1));
10.    int minutes = Integer.parseInt(m.group(2));
11.    int seconds = Integer.parseInt(m.group(3));
12.
13.    return (hours * 3600 + minutes * 60 + seconds);
14. }
```

### 2) `public void adjustSecondsToday(int secondsToday)`

### 3) `public void handleStartPause(Project prj) throws ParseException`
