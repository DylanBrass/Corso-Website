const createJSONForLanguages = (langCodes: string[], texts: string[]) => {

    let langsJSON = {}

    if (texts.length !== langCodes.length)
        throw Error("The keys sent don't match the texts sent.")

    for (let i = 0; i < langCodes.length; i++) {
        // @ts-ignore
        langsJSON[langCodes[i]] = texts[i]
    }

    return JSON.stringify(langsJSON)
}


const getValuesFromJSON = (key : string,json: any) => {
    if (json === undefined || json === ""){
        return json
    }

    if(typeof json !== "string"){
        return json
    }


    try {
        json = JSON.parse(json)
    }catch (e){
        return json
    }

    return json[key] || ""
}


export {createJSONForLanguages, getValuesFromJSON}