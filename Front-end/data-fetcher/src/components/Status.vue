<script>
    import StatusHeader from "./StatusHeader.vue"
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
        created () {
            if (!(this.id === 'error' || this.id === 'success' || this.id === 'updating' || this.id === 'scheduled' )) {
                this.$router.push('/404')
            } 
        },
        props: {
            configList: Array,
        },
        components: {
            StatusHeader
        }
    }
</script>

<template>
    <!-- Components to dislay different configs that matched the given status header -->
    <StatusHeader :label="this.id" class="mx-12 my-6"></StatusHeader>

    <div class="columns-3 overflow-hidden shadow-md shadow-blue-500/50  mx-12">
        <ul class="text-sm font-medium text-black rounded-lg">
            <li v-for="item of configList.filter(config => config.status === this.id)" class="p-1 mx-12">
                <router-link :to="this.id + '/' + item.id">
                    <div class="rounded-lg text-2xl hover:bg-blue-200">
                        {{item.configName }} 
                    </div>
                </router-link>
            </li>
        </ul>
    </div>
</template>