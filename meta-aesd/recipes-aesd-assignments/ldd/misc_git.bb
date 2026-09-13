LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit module

SRC_URI = "git://github.com/shribi/aeld-assignment-7;protocol=ssh;branch=master"
SRCREV = "7d2f02c6b0908991ffdc6202f89766b97d990fe2"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

# Wildcard avoids needing KERNEL_VERSION resolved at parse time
FILES:${PN} += "${nonarch_base_libdir}/modules/*/ldd/*.ko"

# module_do_compile unsets CFLAGS, so pass the include dir via KCFLAGS instead
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR} M=${S}/misc-modules -C ${STAGING_KERNEL_DIR} KCFLAGS=-I${S}/include"

# Skip module_do_install (modules_install) and install the .ko manually
do_install:append() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/ldd
	install -m 0644 ${S}/misc-modules/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/ldd/
}