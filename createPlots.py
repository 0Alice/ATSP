from subprocess import Popen, PIPE, STDOUT
import io
p = Popen(['java', '-jar', '.\\out\\artifacts\\ATSP_jar\\ATSP.jar','test1'], stdout=PIPE, stderr=STDOUT)
for line in io.TextIOWrapper(p.stdout, encoding="utf-8"):
    print(line)