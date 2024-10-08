from configparser import ConfigParser
import google.generativeai as genai

def initialize_model():
    config = ConfigParser()
    config.read('credentials.ini')
    api_key = config['API_KEY']['google_api_key']
    genai.configure(api_key=api_key)
    return genai.GenerativeModel('gemini-pro')

def construct_message(message, role='user'):
    return {'role': role, 'parts': [message]}

def add_model_response(conversation, model):
    response = model.generate_content(conversation, stream=False)
    response.resolve() # wait until all streamed messages are out, not sure what happens when stream=False
    conversation.append(construct_message(response.text, 'model'))
    print(conversation[-1]['parts'][0]) 
    print('-'*45)

def main():
    model_gemini_pro = initialize_model()
    conversation = [
        construct_message('abcd') # start conversation
    ]
    while True: 
        add_model_response(conversation, model_gemini_pro)
        user_input = input("Enter your next message or type 'exit' to end: ")
        if user_input.lower() == 'exit': break
        conversation.append(construct_message(user_input))

main()