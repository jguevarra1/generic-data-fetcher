<script>
    export default {
        data () {
            return {
                msg: "Page not found."
            }
        },
    }
</script>

<template>
    <!-- A component to display not found address -->
    <div class="absolute  h-[66px] top-[130px] left-[400px] rounded-xl text-5xl font-bold">
        <h1 class="text-white text-5xl">
            {{ msg }}
        </h1>
    </div>
</template>