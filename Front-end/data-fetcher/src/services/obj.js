import FormAPI from "./FormAPI"


export default {
     pullRequirement(fileType) {    
        const res = FormAPI.getRequirements(fileType);

        return res;
    },

    xxyy() {
        console.log("HELLO");
        return "YOU"
    }
}