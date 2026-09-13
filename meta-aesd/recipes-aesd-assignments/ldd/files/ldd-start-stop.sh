#!/bin/sh

case "$1" in
    start)
        echo "Loading ldd modules"

        # Load all the modules in the ldd directory of /lib/modules/$(uname -r)/ldd
        for module in /lib/modules/$(uname -r)/ldd/*.ko; do
            echo "Loading module: $(basename $module .ko)"
            insmod /lib/modules/$(uname -r)/ldd/$(basename $module .ko).ko
            MODULE_MAJOR=$(awk "\$2==\"$(basename $module .ko)\" {print \$1}" /proc/devices)
            if [ -n "$MODULE_MAJOR" ]; then
                mknod /dev/$(basename $module .ko) c $MODULE_MAJOR 0
            fi
        done
        ;;
    stop)
        echo "Unloading ldd modules"
        for module in /lib/modules/$(uname -r)/ldd/*.ko; do
            rm -f /dev/$(basename $module .ko)
            rmmod /lib/modules/$(uname -r)/ldd/$(basename $module .ko).ko
        done
        ;;
    *)
        echo "Usage: $0 {start|stop}"
        exit 1
        ;;
esac