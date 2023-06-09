<script>
  import Nav from "./components/Nav.vue"
  import FormAPI from './services/FormAPI'

  export default {
    data () {
      return {
        configList: [], 
      }
    },
    mounted () {
      // Get data from the back-end and populate them in Configlist to display.
      this.getData()
      .then( (data) => {
        for (let item of data.data) {
          this.configList.push(item);
        }
      })
    },
    methods: {
      getData() {
        return FormAPI.getData()
      },
    },
    components: { Nav }
  }
</script>


<template>
  <!-- Nav bar -->
  <Nav></Nav>

  <!-- Different views generated from vue-router -->
  <div class="container mx-auto">
      <router-view :key="$route.fullPath" v-bind:configList="this.configList"/>
  </div>
</template>