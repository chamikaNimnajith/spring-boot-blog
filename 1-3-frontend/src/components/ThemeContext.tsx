import {createContext, useContext, useState, useEffect, ReactNode, useMemo} from "react";

type ThemeContextType = {
    isDarkMode: boolean;
    toggleTheme: () => void;
};

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

type ThemeProviderProps = {
    children: ReactNode;
    initialDarkMode?: boolean;
};

export const ThemeProvider = ({children, initialDarkMode}: ThemeProviderProps) => {
    const [isDarkMode, setIsDarkMode] = useState<boolean>(() => {
        // Check for initialDarkMode prop first
        if (initialDarkMode !== undefined) return initialDarkMode;

        // Then check localStorage
        const savedMode = localStorage.getItem("darkMode");
        if (savedMode !== null) return JSON.parse(savedMode);

        // Finally check system preference
        return window.matchMedia("(prefers-color-scheme: dark)").matches;
    });

    useEffect(() => {
        // Apply class to document element
        document.documentElement.classList.toggle("dark", isDarkMode);

        // Persist to localStorage
        localStorage.setItem("darkMode", JSON.stringify(isDarkMode));
    }, [isDarkMode]);

    const toggleTheme = () => setIsDarkMode((prev) => !prev);

    // Memoize context value to prevent unnecessary re-renders
    const contextValue = useMemo(() => ({isDarkMode, toggleTheme}), [isDarkMode]);

    return <ThemeContext.Provider value={contextValue}>{children}</ThemeContext.Provider>;
};

export const useTheme = (): ThemeContextType => {
    const context = useContext(ThemeContext);
    if (context === undefined) {
        throw new Error("useTheme must be used within a ThemeProvider");
    }
    return context;
};
