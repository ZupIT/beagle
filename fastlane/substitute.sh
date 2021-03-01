match=""
replace=match
file=""
appendToFile=""

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

    if [[ "$appendToFile" == "" ]]
    then
        sed -i -E "s/$match/$replace/g" $file
    else
        sed -E "s/$match/$replace/g" $file >> $appendToFile
    fi
}