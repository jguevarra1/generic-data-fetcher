<script>
  import Notification from '../components/Notification.vue'
  import Success from '../components/Success.vue'
  import Updating from '../components/Updating.vue'
  import Error from '../components/Error.vue'
  import Configlist from "../components/Configlist.vue"

  export default {
      props: {
            configList: Array,
      },
    components: {
      Notification, Success, Updating, Error, Configlist
    }
  }
</script>

<template>
  <!-- Home Page -->
  <div class="md:grid grid-cols-4 gap-12 items-center mx-3 mt-12">
    <Success :configList="this.configList"></Success>
    <Updating :configList="this.configList"></Updating>
    <Error :configList="this.configList" ></Error>
    <Notification class="row-span-2" :configList="this.configList"></Notification>
    <Configlist class="col-span-3" :configList="this.configList"></Configlist>
  </div>
</template>