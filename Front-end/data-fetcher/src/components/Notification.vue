<script>
  import Header from "../components/Header.vue"

  export default {
    props: {
      configList: Array
    },
    components: {
      Header,
    },
    methods : {
      capitalizeFirst(str) {
        return str.charAt(0).toUpperCase() + str.slice(1)
      },

      successfulText() {
        return " was successfully fetched and stored."
      },

      updatingText() {
        return " is being processed at this time."
      },

      errorText() {
        return " encountered an error while processing."
      }
    }
  }
</script>

<template>
  <div class="flex flex-col h-[100%] bg-white shadow-md shadow-blue-500/50 rounded-xl">
    <Header class="mx-12 mt-12" text="Notification"></Header>
    <div>
      <!-- Only display at most 5 recent notifications for every jobs -->
      <ul v-for="item in this.configList.slice(Math.max(this.configList.length - 5, 0))">
          <!-- Success notification  -->
        <div v-if="item.status === 'success'">
          <li class="text-emerald-800 text-bold text-base mx-12 my-2"> <span class="text-black 2">{{item.configName}}</span>{{successfulText()}} </li>
        </div>

        <!-- Updating notification -->
        <div v-if="item.status === 'updating'">
          <li class="text-yellow-800 text-bold text-base mx-12 my-2"> <span class="text-black 2">{{item.configName}}</span>{{updatingText()}} </li>
        </div>

        <!-- Error notification -->
        <div v-if="item.status === 'error'">
          <li class="text-red-800 text-bold text-base mx-12 my-2"> <span class="text-black 2">{{item.configName}}</span>{{errorText()}} </li>
        </div>

      </ul>
    </div>

  </div>
</template>