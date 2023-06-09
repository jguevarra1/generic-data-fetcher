<script>
import FormAPI from "../services/FormAPI";
import Input from "./Input.vue";
import Header from "./Header.vue"
import PopUpForm from './PopUpForm.vue';
import DropDown from './DropDown.vue';

export default {
    props: {
        configList: Array
    },
    data() {
        return {
            fileType: '',
            configName: '',
            storage: '',
            status: 'updating',
            description: '',
            fileTypeArray: ['Zip File', 'Text File', 'HTML Table', 'HTML Unordered List'],
            storageArray: ['Amazon S3', 'Google Drive'],
            fetchingInfos: [],
            toSend: {}, 
            sendingStorageInfo: {}, 
            toRender: new Map(), 
            storageInfo: new Map(),
            parseInfo: {
                listOfFiles: new Array(),
            },
        }
    },
    watch: {
        // When fileType changes, update pop-up form by pulling data from back-end
        fileType(newFileType, oldFileType) {
            let value = '';
            this.toRender = new Map();
            this.toSend = {};

            switch (newFileType) {
                case ('Text File'):
                    value = 'textFiles'
                    break;
                case ('Zip File'):
                    value = 'zipFiles'
                    break;
                case ('HTML Table'):
                    value = 'htmlTables'
                    break;
                case ('HTML Unordered List'):
                    value = 'htmlLists'
                    break;
            }

            if (newFileType !== "Choose File Type") {
                const res = FormAPI.getRequirements(value)
                    .then((resp) => {
                    for (let key in resp.data) {
                        this.toRender.set(key, resp.data[key]);
                        this.toSend[key] = "";
                        if (key === "storage") {
                            this.toSend[key] = "Amazon S3";
                        }
                    }
                });
            }
        },

        // When storage changes, call back-end and update storage information.
        storage(newStorage, oldStorage) {
            this.storageInfo = new Map();
            this.sendingStorageInfo = {};
            let value = newStorage === 'Amazon S3' ? 'amazon' : 'googleDrive'
            if (newStorage !== 'Choose Storage') {
                const res = FormAPI.getRequirements(value)
                .then((res) => {
                    for (let key in res.data) {
                        this.storageInfo.set(key, res.data[key]);
                        this.sendingStorageInfo[key] = "";
                    }
                })
            }
        }
    },
    methods : {
        // Send data to back-end
        sendData() {
            // Populate fetchingInfos
            for (const item of this.fetchingInfos) {
                let myTarget = JSON.parse(JSON.stringify(item));
                this.parseInfo.listOfFiles.push(myTarget);
            }

            const response = FormAPI.sendForm(JSON.stringify({
                configName: this.configName,
                fileType: this.fileType,
                description: this.description,
                status: this.status,
                parseInfo: this.parseInfo,
                storage: {
                    storageName: this.storage,
                    storageInfo: this.sendingStorageInfo
                }
            }))
            .then((res) => {
                // Update the configList on the front-end.
                this.configList.push(res.data);
            })
            .catch((res) => {
                // Display error if not successful.
                console.log(res);
            })

            // Go to homepage
            this.$router.push('/');
        },
        /**
         * Convert the string to look better when displaying.
         * 
         * For example: to_display -> To Display
         * (Break the string by _ -> capitalize first on each word)
         */
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
    },
    components: { Input, Header, PopUpForm, DropDown}
}
</script>

<template>
    <!-- Upper half for general information -->
    <div class="mt-3 h-[50vh] grid grid-cols-3 grid-rows-5 grid-flow-col shadow-lg shadow-blue-500/50 rounded-xl">
        <Input :maxLength="128" v-model="this.configName" label="Job Name"></Input>

        <Input :maxLength="64" v-model="this.description" label="Description"></Input>
        
        <div class="row-span-4">
            <DropDown label="File Type" v-model="fileType" disabled_value="Choose File Type" :options='this.fileTypeArray'></DropDown>        
        </div>
        
        <div class="row-span-3">
            <DropDown label="Storage" v-model="storage" disabled_value="Choose Storage" :options='this.storageArray'></DropDown>
        </div>

        <!-- Storage Info -->  
        <div v-for="[key, value] in this.storageInfo" class="col-start-5">
            <Input :inputType="value" v-model="this.sendingStorageInfo[key]" :label="this.converter(key)"></Input>
        </div>
    </div>

    <!-- Bottom Half for fetching information -->
    <div class="flex justify-end mt-3 ">
        <!-- Popup form includes a button to trigger it -->
        <PopUpForm :listToSend="this.fetchingInfos" :toSend="toSend" :formInfo="toRender" :fileType="this.fileType" :list="this.fetchingInfos"></PopUpForm>

        <!-- Save button -->
        <button
            @click="sendData"
            class="text-white bg-black focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center mx-3"
            type="button">
            Save
        </button>
    </div>

    <!-- Display fetching information after user click save. -->
    <div class="mt-3 h-[30vh] border-indigo-500/50 shadow-lg shadow-blue-500/50 rounded-xl">
        <div class="grid grid-cols-2 grid-rows-5 grid-flow-col">
            <ol v-for="info in this.fetchingInfos">
                <div class="mt-3 mx-3 ">
                    <span class="text-xl text-indigo-500">Fetching Info: <span class="text-black">{{info.URL}}</span> </span>
                </div>
            </ol>
        </div>
    </div>    
</template>