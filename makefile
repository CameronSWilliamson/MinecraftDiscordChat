compile:
	gradle shadowJar

mv:
	bash ./move_file.sh

cmpmv: compile mv
