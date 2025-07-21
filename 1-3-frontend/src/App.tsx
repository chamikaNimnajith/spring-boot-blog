import "./App.css";
import {BrowserRouter, Routes, Route, Navigate, useLocation} from "react-router-dom";
import {AnimatePresence, motion} from "framer-motion";
import NavBar from "./components/NavBar";
import HomePage from "./pages/HomePage";
import EditPostPage from "./pages/EditPostPage";
import PostPage from "./pages/PostPage";
import CategoriesPage from "./pages/CategoriesPage";
import TagsPage from "./pages/TagsPage";
import DraftsPage from "./pages/DraftsPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import {AuthProvider, useAuth} from "./components/AuthContext";
import {ThemeProvider} from "./components/ThemeContext";

// Type definitions for better type safety
type ProtectedRouteProps = {
    children: React.ReactNode;
};

type UserProfile = {
    name: string;
    avatar?: string;
};

const ProtectedRoute = ({children}: ProtectedRouteProps) => {
    const {isAuthenticated} = useAuth();

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    return <>{children}</>;
};

// Animation variants controlling opacity and vertical slide
const pageVariants = {
    initial: {
        opacity: 0,
        y: 16,
    },
    enter: {
        opacity: 1,
        y: 0,
    },
    exit: {
        opacity: 0,
        y: -16,
    },
};

// Transition configuration with easing tuned for natural feel
const pageTransition = {
    type: "tween",
    ease: [0.33, 1, 0.68, 1], // OutQuad-like feel for snappy yet smooth transitions
    duration: 0.4, // Keeps it snappy but noticeable
};

// Reusable animated wrapper for route-level transitions
const AnimatedRoute: React.FC<{children: React.ReactNode}> = ({children}) => (
    <motion.div
        initial="initial"
        animate="enter"
        exit="exit"
        variants={pageVariants}
        transition={pageTransition}
        style={{
            willChange: "opacity, transform",
            transformOrigin: "center top",
        }}
    >
        {children}
    </motion.div>
);

const AppContent = () => {
    const {isAuthenticated, logout, user} = useAuth();
    const location = useLocation();

    const userProfile: UserProfile | undefined = user
        ? {
              name: user.name,
              avatar: undefined,
          }
        : undefined;

    return (
        <>
            <NavBar isAuthenticated={isAuthenticated} userProfile={userProfile} onLogout={logout} />

            <main className="container mx-auto py-6">
                <AnimatePresence mode="wait">
                    <Routes location={location} key={location.pathname}>
                        <Route
                            path="/"
                            element={
                                <AnimatedRoute>
                                    <HomePage />
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/login"
                            element={
                                <AnimatedRoute>
                                    <LoginPage />
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/signup"
                            element={
                                <AnimatedRoute>
                                    <SignupPage />
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/posts/new"
                            element={
                                <AnimatedRoute>
                                    <ProtectedRoute>
                                        <EditPostPage />
                                    </ProtectedRoute>
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/posts/:id"
                            element={
                                <AnimatedRoute>
                                    <PostPage isAuthenticated={isAuthenticated} currentUserId={user?.id} />
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/posts/:id/edit"
                            element={
                                <AnimatedRoute>
                                    <ProtectedRoute>
                                        <EditPostPage />
                                    </ProtectedRoute>
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/categories"
                            element={
                                <AnimatedRoute>
                                    <CategoriesPage isAuthenticated={isAuthenticated} />
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/tags"
                            element={
                                <AnimatedRoute>
                                    <TagsPage isAuthenticated={isAuthenticated} />
                                </AnimatedRoute>
                            }
                        />

                        <Route
                            path="/posts/drafts"
                            element={
                                <AnimatedRoute>
                                    <ProtectedRoute>
                                        <DraftsPage />
                                    </ProtectedRoute>
                                </AnimatedRoute>
                            }
                        />
                    </Routes>
                </AnimatePresence>
            </main>
        </>
    );
};

const App = () => {
    return (
        <BrowserRouter>
            <ThemeProvider>
                <AuthProvider>
                    <AppContent />
                </AuthProvider>
            </ThemeProvider>
        </BrowserRouter>
    );
};

export default App;
