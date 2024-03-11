import re;
from datetime import datetime, timezone, timedelta;

total_pattern = r'[0-9]{,3}\.[0-9]{,3}\.[0-9]{,3}\.[0-9]{,3}.*[0-9]{2,2}\/.{3,4}\/[0-9]{4,4}:[0-9][0-9]:[0-9][0-9]:[0-9][0-9] \+[0-9]{4,4}';
ip_pattern = r'[0-9]{,3}\.[0-9]{,3}\.[0-9]{,3}\.[0-9]{,3}';
date_pattern = r'[0-9]{2,2}\/.{3,4}\/[0-9]{4,4}:[0-9][0-9]:[0-9][0-9]:[0-9][0-9] \+[0-9]{4,4}';

def calculate_elapsed(old, new):

    old_obj = parse_date_into_object(old);
    new_obj = parse_date_into_object(new);

    datetime_old = datetime(old_obj['year'], old_obj['month'], old_obj['day'], old_obj['hours'], old_obj['minutes'], old_obj['seconds']); 
    datetime_new = datetime(new_obj['year'], new_obj['month'], new_obj['day'], new_obj['hours'], new_obj['minutes'], new_obj['seconds']);

    elapsed = datetime_new.timestamp() - datetime_old.timestamp(); 

    return elapsed;

def parse_date_into_object(date):
    match = re.match(date_pattern, date)
    date_string = match.group();
    
    month_dict = {
    'Jan': 1,
    'Feb': 2,
    'Mar': 3,
    'Apr': 4,
    'May': 5,
    'Jun': 6,
    'Jul': 7,
    'Aug': 8,
    'Sep': 9,
    'Oct': 10,
    'Nov': 11,
    'Dec': 12,
    }

    if match:

        day = date_string[0:2];
        month = date_string[3:6];
        year = date_string[7:11];
        hours = date_string[12:14];
        minutes = date_string[15:17];
        seconds = date_string[18:20];
        offset = date_string[22:26];


        return{
            'day': int(day),
            'month': month_dict[month],
            'year': int(year),
            'hours':int(hours),
            'minutes':int(minutes),
            'seconds':int(seconds),
            'offset_h':int(offset[1:3]),
            'offset_m':int(offset[3:5])
        };


def time_elapsed():
  
    ips_dict_date = {};
    ips_dict_elapsed = {};
    ips_list = [];

    with open("log_q1.txt", 'r') as file:
        for  line in file:        
            total = re.findall(total_pattern, line);
            if len(total) != 0:
                dates = re.findall(date_pattern, total[0]);
                ips = re.findall(ip_pattern, total[0]);
                if ips[0] in ips_dict_date:
                    ips_dict_elapsed[ips[0]] = calculate_elapsed(ips_dict_date[ips[0]], dates[0]);
                    ips_list.append(f"{str(ips[0])}  {str(int(ips_dict_elapsed[ips[0]]))}");
                else:
                    ips_dict_elapsed[ips[0]] = 0;
                    ips_list.append(f"{str(ips[0])} 0");
                    ips_dict_date[ips[0]] = dates[0];
    

    with open("output_q1.txt", "w") as output:
        for item in ips_list:
            output.write(f"{item}\n");


time_elapsed();
