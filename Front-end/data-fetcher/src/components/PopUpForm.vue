<script>
    import Input from "../components/Input.vue"
    import FormAPI from "../services/FormAPI"
    import obj from "../services/obj"

    export default {
        props: {
            list: Array,
            fileType: String,
            formInfo: Map,
            toSend: Object,
            listToSend: Array,
        },
        data() {
            return {
                msg: "Hello",
                delimiter: '',
                header: '',
                fileName: '',
                localPath: '',
                supportedFiles: ['textFiles', 'zipFiles', 'htmlTables', 'htmlLists'],
                textFiles: {},
                zipFiles: {},
                htmlTables: {},
                htmlLists: {},
                configMap: new Map(),
                inputDescription: new Map([
                    ['headers',  'Header of the file to fetch'],
                    ['URL',  'URL to the download site'],
                    ['delimiter',  "File's delimiter separted by comma"],
                    ['table_index',  'Index of the table starting from 0'],
                    ['selector', 'The outter div that wraps the list'],
                    ['list_index', 'Index of the list starting from 0'],
                    ['file_name', 'Name of the file needed to fetch']
                ]),
            }
        },
        components: {
            Input
        },
        methods: {
            test() {
                const returnedTarget = Object.assign({}, this.toSend);
                this.listToSend.push(returnedTarget);

                for (let key in this.toSend) {
                    this.toSend[key] = ''
                }
            },

            async getReqs(fileType) {
                let promise = await obj.pullRequirement(fileType);

                switch (fileType) {
                    case 'textFiles':
                        this.textFiles = promise.data;
                        break;
                    case 'zipFiles':
                        this.zipFiles = promise.data;
                        break;
                    case 'htmlTables':
                        this.htmlTables = promise.data;
                        break;
                    case 'htmlLists':
                        this.htmlLists = promise.data;
                        break;
                }


                this.configMap.set('textFiles', this.textFiles);
                this.configMap.set('zipFiles', this.zipFiles);
                this.configMap.set('htmlTables', this.htmlTables);
                this.configMap.set('htmlLists', this.htmlLists);
                
                return FormAPI.getRequirements(fileType);
            },
            converter(string) {
                if (string !== string.toUpperCase()) {
                    string = string.toLowerCase().split('_');
                    for (var i = 0; i < string.length; i++) {
                        string[i] = string[i].charAt(0).toUpperCase() + string[i].substring(1);     
                    }
                    return string.join(' ');
                }
                return string;
            }
        }
    }
</script>

<template>
    <button
        class="bg-black text-white focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
        type="button" data-modal-toggle="authentication-modal">
        New Fetching Information
    </button>

    <div id="authentication-modal" tabindex="-1" aria-hidden="true"
        class="fixed top-0 left-0 right-0 z-50 hidden w-full overflow-x-hidden overflow-y-auto md:inset-0 h-modal md:h-full justify-center items-center">
        <div class="relative w-full h-full max-w-md p-4 md:h-auto">

            <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
                <!-- Submit button -->
                <button type="button"
                    class="absolute top-3 right-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center dark:hover:bg-gray-800 dark:hover:text-white"
                    data-modal-toggle="authentication-modal">
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd"
                            d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                            clip-rule="evenodd"></path>
                    </svg>
                </button>

                <!-- Form pulled from back-end -->
                <div class="px-6 py-6 lg:px-8">
                    <h3 class="mb-4 text-xl font-medium text-gray-900 dark:text-white">{{fileType}}</h3>
                    <form class="space-y-6" v-on:submit.prevent="test">
                         <div v-for="[key, value] in this.formInfo">
                            <Input :inputType="value" v-model="this.toSend[key]" :label="this.converter(key)" :placeholder="this.inputDescription.get(key)"></Input>
                        </div>
                        <button
                            class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</template>