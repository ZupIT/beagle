substitute() {
    match=""
    replace=match
    file=""
    inPlace=""

    while [ $# -gt 0 ]
    do
        if [[ "$1" == **"--"** ]]
        then
            param="${1/--/}"
            declare "$param"="$2"
        fi
        shift
    done

    if [[ "$inPlace" != "false" ]]
    then
        sed -i -E "s/$match/$replace/g" "$file"
    else
        sed -E "s/$match/$replace/g" "$file"
    fi
}