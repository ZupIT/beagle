match=""
replace=match
file=""
appedTo=""

substitute() {
    while [ $# -gt 0 ]
    do
        if [[ $1 == **"--"** ]]
        then
            param="${1/--/}"
            declare $param="$2"
        fi
        shift
    done

    if [[ "$appendTo" == "" ]]
    then
        sed -i -E "s/$match/$replace/g" $file
    else [[ "$appendTo" != "" ]]
        sed -E "s/$match/$replace/g" $file >> $appendTo
    fi
}