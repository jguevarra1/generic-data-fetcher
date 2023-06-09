<script>
    import Header from "./Header.vue"
    export default {
        methods: {
            capitalizeFirst(str) {
                return str.charAt(0).toUpperCase() + str.slice(1)
            }
        },
        data () {
            return {
                id: this.$route.params.id 
            }
        },
        props: {
            configList: Array,
        },
        components: {
            Header
        }
    }
</script>

<template>
<Header text="All Jobs" class="mt-12 mx-12 text-indigo-900"></Header>

<div class="columns-3 overflow-hidden shadow-md shadow-blue-500/50  mx-12">
    <ul class="text-sm font-medium text-black rounded-lg">
        <li v-for="item of this.configList" class="p-1 mx-12">
            <router-link :to="'/status/' + item.status + '/' + item.id">
                <div class="rounded-lg text-2xl hover:bg-blue-200">
                    {{item.configName }} 
                </div>
            </router-link>
        </li>
    </ul>
</div>
    
</template>