import React from "react";
import {Link, useLocation} from "react-router-dom";
import {
    Navbar,
    NavbarBrand,
    NavbarContent,
    NavbarItem,
    NavbarMenu,
    NavbarMenuItem,
    NavbarMenuToggle,
    Button,
    Avatar,
    Dropdown,
    DropdownTrigger,
    DropdownMenu,
    DropdownItem,
    Switch,
} from "@nextui-org/react";
import {Plus, Edit3, LogOut, User, BookDashed, Moon, Sun} from "lucide-react";
import {useTheme} from "../components/ThemeContext";
import {motion} from "framer-motion";

interface NavBarProps {
    isAuthenticated: boolean;
    userProfile?: {name: string; avatar?: string};
    onLogout: () => void;
}

const NavBar: React.FC<NavBarProps> = ({isAuthenticated, userProfile, onLogout}) => {
    const location = useLocation();
    const {isDarkMode, toggleTheme} = useTheme();
    const [isMenuOpen, setIsMenuOpen] = React.useState(false);

    const navLinks = [
        {label: "Home", path: "/"},
        {label: "Categories", path: "/categories"},
        {label: "Tags", path: "/tags"},
    ];

    const isActive = (path: string) => location.pathname === path;

    const NavLink = ({label, path}: {label: string; path: string}) => (
        <NavbarItem isActive={isActive(path)}>
            <Link
                to={path}
                className={`relative text-sm font-medium transition-colors ${
                    isActive(path) ? "text-primary" : "text-default-600 hover:text-primary"
                }`}
            >
                {label}
                {isActive(path) && (
                    <motion.div
                        layoutId="nav-underline"
                        className="absolute -bottom-1 left-0 h-[2px] w-full bg-primary rounded-full"
                        transition={{type: "spring", stiffness: 500, damping: 30}}
                    />
                )}
            </Link>
        </NavbarItem>
    );

    return (
        <Navbar isBordered isMenuOpen={isMenuOpen} onMenuOpenChange={setIsMenuOpen} className="mb-6">
            <NavbarContent className="sm:hidden" justify="start">
                <NavbarMenuToggle />
            </NavbarContent>

            <NavbarContent className="sm:hidden pr-3" justify="center">
                <NavbarBrand>
                    <Link to="/" className="text-xl font-bold">
                        Crawling
                    </Link>
                </NavbarBrand>
            </NavbarContent>

            <NavbarContent className="hidden sm:flex gap-6" justify="start">
                <NavbarBrand>
                    <Link to="/" className="text-xl font-bold">
                        Crawling
                    </Link>
                </NavbarBrand>
                {navLinks.map((link) => (
                    <NavLink key={link.path} label={link.label} path={link.path} />
                ))}
            </NavbarContent>

            <NavbarContent justify="end" className="gap-2">
                {isAuthenticated ? (
                    <>
                        <NavbarItem>
                            <Button
                                as={Link}
                                to="/posts/drafts"
                                color="secondary"
                                variant="light"
                                size="sm"
                                startContent={<BookDashed size={16} />}
                            >
                                Drafts
                            </Button>
                        </NavbarItem>
                        <NavbarItem>
                            <Button
                                as={Link}
                                to="/posts/new"
                                color="primary"
                                size="sm"
                                startContent={<Plus size={16} />}
                            >
                                New Post
                            </Button>
                        </NavbarItem>
                        <NavbarItem>
                            <Dropdown placement="bottom-end">
                                <DropdownTrigger>
                                    <Avatar
                                        isBordered
                                        as="button"
                                        className="transition-transform hover:scale-105"
                                        src={userProfile?.avatar}
                                        name={userProfile?.name}
                                    />
                                </DropdownTrigger>
                                <DropdownMenu aria-label="User Menu">
                                    <DropdownItem key="profile" startContent={<User size={16} />}>
                                        <Link to="/profile">Profile</Link>
                                    </DropdownItem>
                                    <DropdownItem key="drafts" startContent={<Edit3 size={16} />}>
                                        <Link to="/posts/drafts">My Drafts</Link>
                                    </DropdownItem>
                                    <DropdownItem
                                        key="logout"
                                        startContent={<LogOut size={16} />}
                                        color="danger"
                                        onPress={onLogout}
                                    >
                                        Log Out
                                    </DropdownItem>
                                </DropdownMenu>
                            </Dropdown>
                        </NavbarItem>
                        <NavbarItem>
                            <Switch
                                isSelected={isDarkMode}
                                onChange={toggleTheme}
                                size="sm"
                                color="secondary"
                                thumbIcon={({isSelected}) => (isSelected ? <Moon size={16} /> : <Sun size={16} />)}
                            />
                        </NavbarItem>
                    </>
                ) : (
                    <>
                        <NavbarItem className="hidden sm:flex">
                            <Button as={Link} to="/signup" variant="light" size="sm">
                                Sign Up
                            </Button>
                        </NavbarItem>
                        <NavbarItem>
                            <Button as={Link} to="/login" color="primary" size="sm">
                                Log In
                            </Button>
                        </NavbarItem>
                    </>
                )}
            </NavbarContent>

            <NavbarMenu>
                {navLinks.map((link) => (
                    <NavbarMenuItem key={link.path}>
                        <Link
                            to={link.path}
                            className={`block py-2 w-full text-base font-medium ${
                                isActive(link.path) ? "text-primary" : "text-default-600 hover:text-primary"
                            }`}
                            onClick={() => setIsMenuOpen(false)}
                        >
                            {link.label}
                        </Link>
                    </NavbarMenuItem>
                ))}
                {!isAuthenticated && (
                    <>
                        <NavbarMenuItem>
                            <Link
                                to="/signup"
                                className="block py-2 w-full text-default-600 hover:text-primary"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Sign Up
                            </Link>
                        </NavbarMenuItem>
                        <NavbarMenuItem>
                            <Link
                                to="/login"
                                className="block py-2 w-full text-default-600 hover:text-primary"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Log In
                            </Link>
                        </NavbarMenuItem>
                    </>
                )}
            </NavbarMenu>
        </Navbar>
    );
};

export default NavBar;
