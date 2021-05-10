def compare(actual, expected):
    if actual == expected:
        return "PASS"
    else:
        return "FAIL! (expected " + str(expected) + " got " + str(actual) + ")"

class AI:
    def __init__(self, name, health):
        self.name = name
        self.health = health
    
    def get_name(self):
        return self.name
    
    def get_health(self):
        # check whether health < 0?
        return self.health
    
    def is_dead(self):
        return self.health == 0

class EvilAI(AI):
    def __init__(self, name, health):
        self.name = name
        self.health = health
        self.full_health = health
        self.clonable = True
        self.aliveclones = 1
        self.master = self
    
    def clone(self):
        if self.is_dead():
            return None
        else:
            if self.clonable:
                self.master.aliveclones += 1
                if self.master != self:
                    self.aliveclones += 1
                temp = EvilAI(self.name,self.full_health)
                temp.master = self
                return temp
    
    def count_copies(self):
        return self.aliveclones
    
    def is_dead(self):
        mmdc = 0
        temp = self
        # checking the mastermind
        while temp.master != temp:
            if temp.health == 0:
                mmdc += 1
            temp = temp.master
        temp.aliveclones -= mmdc
        return temp.count_copies() == 0
            
    
    def attack(self, target):
        if self.health != 0: # the clone may attack so don't use is_dead()
            if not target.is_dead():
                target.health -= 1

class SuperAI(AI):
    def __init__(self, name, health):
        self.name = name
        self.health = health
        self.full_health = health
    
    def attack(self, target):
        if not target.is_dead():
            target.health = max(target.health-5,max(0,target.health-5))
    
    def cripple(self, target):
        if not target.is_dead():
            target.health -= 1
            target.clonable = False
            


# Tests
def test_q3():
    print("Testing Q3")
    jarvis = AI("Jarvis", 2)
    ultron = EvilAI("Ultron", 10)
    vision = SuperAI("Vision", 5)

    ultron.attack(jarvis)
    print("jarvis.get_health():", compare(jarvis.get_health(), 1))
    ultron.attack(jarvis)
    print("jarvis.is_dead():", compare(jarvis.is_dead(), True))

    u1 = ultron.clone()
    u2 = ultron.clone()
    u3 = ultron.clone()
    print("ultron.count_copies():", compare(ultron.count_copies(), 4))

    vision.attack(u1)
    vision.attack(u2)
    vision.attack(u1)
    print("u1.is_dead():", compare(u1.is_dead(), False))
    # ultron in full health, u1 in 0 health, u2 in 5 health, u3 in full health
    print("ultron.count_copies():", compare(ultron.count_copies(), 3))

    ultron.attack(vision)
    u1.attack(vision)
    u2.attack(vision)
    print("vision.get_health():", compare(vision.get_health(), 3))

    vision.attack(ultron)
    vision.attack(ultron)
    print("ultron.is_dead():", compare(ultron.is_dead(), False))

    vision.cripple(u2)
    u4 = u2.clone()
    vision.attack(u2)
    print("u2.get_health():", compare(u2.get_health(), 0))
    print("u3.count_copies():", compare(u3.count_copies(), 1))

# Uncomment to test question 3
test_q3()
