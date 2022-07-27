#!/usr/local/bin/fish

set LINKS $(cat ./list.txt)

set num $(ls | grep -oE "[0-9]*" | sort -r | head -n 1);
set num (math $num + 1);

for link in $LINKS;
    set name $(echo $link | grep -oE "[a-zA-Z]*\.pdf"); 
    set name "$num"_"$name";
    wget $link -O $name -o /dev/null &;
    set num (math $num + 1);
end;

