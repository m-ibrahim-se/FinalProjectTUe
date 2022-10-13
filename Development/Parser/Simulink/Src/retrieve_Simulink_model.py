import matlab.engine


def main_function():
    eng = matlab.engine.start_matlab()
    file_count = eng.Parser_Simulink_Model(nargout=1)
    print(file_count)
    print("Simulink parser executed successfully")
    eng.quit()
    return file_count

